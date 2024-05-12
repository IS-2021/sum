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

export function useAuthRedirect({
  protectedRoutes,
  guestRoutes,
  onGuestRedirect,
  onAuthenticatedRedirect,
}: UseAuthRedirectorProps) {
  const router = useRouter();
  const { isLoaded, isSignedIn } = useUser();

  watchEffect(async () => {
    if (!isLoaded.value) {
      return;
    }

    const currentRoute = router.currentRoute.value.name;
    const isAuthRouteMatching = protectedRoutes.includes(currentRoute);
    const isGuestRouteMatching = guestRoutes?.includes(currentRoute);

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
