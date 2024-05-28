import { useUser } from '@/composables/useUser';
import { computed, watch, watchEffect } from 'vue';
import { useRouter } from 'vue-router/auto';
import type { AppRouteNames, AppRoutes } from '@/lib/router';
import { Role } from '@/lib/api-model';

interface UseAuthRedirectorProps {
  /**
   * A callback executed on redirect of unauthenticated user.
   */
  onGuestRedirect?: () => void;
  /**
   * Routes that will require a non-authenticated user.
   */
  guestRoutes?: AppRouteNames[];
}

// TODO: change default for admin after adding admin route
const defaultRoutesByRole: Record<Role, AppRouteNames> = {
  ROLE_USER: '/',
  ROLE_RESTAURANT: '/manage/',
  ROLE_ADMIN: '/',
};

const roleBasedRoutes: Record<Role, AppRouteNames[]> = {
  ROLE_USER: [
    '/',
    '/onboarding/',
    '/favourites/',
    '/restaurants/',
    '/restaurant/[id]',
    '/restaurant/[...]',
    '/bookings/',
    '/activeBooking/',
    '/settings/',
  ],
  ROLE_RESTAURANT: [
    '/manage/',
    '/manage/bookings/',
    '/manage/meals/',
    '/manage/reports/',
    '/manage/settings/',
  ],
  ROLE_ADMIN: [],
};

/**
 * Redirects user based on their authentication status.
 * Regular users without a complete profile will be redirected to the onboarding page.
 * Guest
 */
export function useAuthRedirect({ guestRoutes, onGuestRedirect }: UseAuthRedirectorProps) {
  const router = useRouter();
  const { user, isLoaded, isSignedIn, isProfileComplete } = useUser();

  const currentRoute = computed(() => router.currentRoute.value.name);

  async function redirectTo(to: AppRoutes, onRedirect?: () => void) {
    await router.push(to);

    if (onRedirect) {
      onRedirect();
    }
  }

  async function profileCompletionGuard() {
    if (!isLoaded.value) {
      return;
    }

    switch (currentRoute.value) {
      case '/onboarding/':
        if (isProfileComplete.value) {
          await redirectTo('/');
        }
        break;
      default:
        if (!isProfileComplete.value) {
          await redirectTo('/onboarding/');
        }
        break;
    }
  }

  watch([isLoaded, currentRoute, isProfileComplete], profileCompletionGuard);

  watchEffect(async () => {
    if (!isLoaded.value || !currentRoute.value) {
      return;
    }

    const userRole = user.value?.role ?? Role.ROLE_USER;
    const allowedRoutes = roleBasedRoutes[userRole];
    const defaultRoute = defaultRoutesByRole[userRole];

    console.log('currentRoute', currentRoute.value);

    const isGuestRouteMatching = guestRoutes?.includes(currentRoute.value);

    if (!isSignedIn.value && !isGuestRouteMatching) {
      console.log('redirect cause: not signed in');
      await redirectTo('/sign-in', onGuestRedirect);
    } else if (isSignedIn.value && isGuestRouteMatching) {
      console.log('redirect cause: signed in');
      await redirectTo(defaultRoute);
    } else if (!allowedRoutes.includes(currentRoute.value)) {
      console.log('redirect cause: not allowed');
      await redirectTo(defaultRoute);
    }
  });
}
