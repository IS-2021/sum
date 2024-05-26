import { useUser } from '@/composables/useUser';
import { watchEffect } from 'vue';
import { useRouter } from 'vue-router/auto';
import type { AppRouteNames } from '@/lib/router';

interface UseAuthRedirectorProps {
  /**
   * Routes that will require an authenticated user.
   */
  protectedRoutes: AppRouteNames[];
  /**
   * A callback executed on redirect of unauthenticated user.
   */
  onGuestRedirect?: () => void;
  /**
   * Routes that will require a non-authenticated user.
   */
  guestRoutes?: AppRouteNames[];
  /**
   * A callback executed on redirect of non-authenticated user.
   */
  onAuthenticatedRedirect?: () => void;
}

/**
 * Redirects user based on their authentication status.
 * Regular users without a complete profile will be redirected to the onboarding page.
 * Guest
 */
export function useAuthRedirect({
  protectedRoutes,
  guestRoutes,
  onGuestRedirect,
  onAuthenticatedRedirect,
}: UseAuthRedirectorProps) {
  const router = useRouter();
  const { isLoaded, isSignedIn, isProfileComplete } = useUser();

  watchEffect(async () => {
    if (!isLoaded.value) {
      return;
    }

    const currentRoute = router.currentRoute.value.name;
    const isAuthRouteMatching = protectedRoutes.includes(currentRoute);
    const isGuestRouteMatching = guestRoutes?.includes(currentRoute);
    const isOnboardingRouteMatching = currentRoute === '/onboarding/';

    if (!isProfileComplete.value && !isOnboardingRouteMatching) {
      await router.push('/onboarding/');
    } else if (isProfileComplete.value && isOnboardingRouteMatching) {
      await router.push('/');
    }

    if (isAuthRouteMatching && !isSignedIn.value) {
      await router.push('/sign-in');

      if (onGuestRedirect) {
        onGuestRedirect();
      }
    } else if (isGuestRouteMatching && isSignedIn.value) {
      await router.push('/');

      if (onAuthenticatedRedirect) {
        onAuthenticatedRedirect();
      }
    }
  });
}
