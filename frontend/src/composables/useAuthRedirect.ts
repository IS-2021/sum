import { useUser } from '@/composables/useUser';
import { watchEffect } from 'vue';
import { useRouter } from 'vue-router/auto';
import type { AppRouteNames, AppRoutes } from '@/lib/router';
import { Role } from '@/lib/api-model';

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

const defaultRoutesByRole: Record<Role, AppRouteNames> = {
  ROLE_USER: '/',
  ROLE_RESTAURANT: '/manage/',
  ROLE_ADMIN: '/',
};

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
  const { user, isLoaded, isSignedIn, isProfileComplete } = useUser();

  async function redirectTo(to: AppRoutes, onRedirect?: () => void) {
    await router.push(to);

    if (onRedirect) {
      onRedirect();
    }
  }

  watchEffect(async () => {
    if (!isLoaded.value) {
      return;
    }

    const currentRoute = router.currentRoute.value.name;
    const isAuthRouteMatching = protectedRoutes.includes(currentRoute);
    const isGuestRouteMatching = guestRoutes?.includes(currentRoute);
    const isOnboardingRouteMatching = currentRoute === '/onboarding/';

    if (!isProfileComplete.value && !isOnboardingRouteMatching) {
      await redirectTo('/onboarding/');
    } else if (isProfileComplete.value && isOnboardingRouteMatching) {
      await redirectTo('/');
    }

    if (isAuthRouteMatching && !isSignedIn.value) {
      await redirectTo('/sign-in', onGuestRedirect);
    } else if (isGuestRouteMatching && isSignedIn.value) {
      const defaultRoute = defaultRoutesByRole[user.value?.role ?? Role.ROLE_USER];
      await redirectTo(defaultRoute, onAuthenticatedRedirect);
    }
  });
}
