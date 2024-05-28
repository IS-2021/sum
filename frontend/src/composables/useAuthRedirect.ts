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
  onDisallowedRouteRedirect?: () => void;
}

// TODO: change default for admin after adding admin route
const defaultRoutesByRole: Record<Role | 'GUEST', AppRouteNames> = {
  GUEST: '/sign-in/',
  ROLE_USER: '/',
  ROLE_RESTAURANT: '/manage/',
  ROLE_ADMIN: '/',
};

const roleBasedRoutes: Record<Role | 'GUEST', AppRouteNames[]> = {
  GUEST: ['/sign-in/', '/sign-up/'],
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
export function useAuthRedirect({
  onGuestRedirect,
  onDisallowedRouteRedirect,
}: UseAuthRedirectorProps) {
  const router = useRouter();
  const { user, isLoaded, isSignedIn, isProfileComplete } = useUser();

  const userRole = computed(() => user.value?.role ?? 'GUEST');

  const currentRoute = computed(() => router.currentRoute.value.name);
  const allowedRoutes = computed(() => roleBasedRoutes[user.value?.role ?? 'GUEST']);
  const isCurrentRouteAllowed = computed(() => allowedRoutes.value.includes(currentRoute.value));
  const defaultRoute = computed(() => defaultRoutesByRole[user.value?.role ?? 'GUEST']);

  async function redirectTo(to: AppRoutes, onRedirect?: () => void) {
    await router.push(to);

    if (onRedirect) {
      onRedirect();
    }
  }

  async function profileCompletionGuard() {
    if (!isLoaded.value || !isSignedIn.value || userRole.value === 'GUEST') {
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

    if (isSignedIn.value && !isCurrentRouteAllowed.value) {
      await redirectTo(defaultRoute.value);
    } else if (!isSignedIn.value && !isCurrentRouteAllowed.value) {
      await redirectTo('/sign-in');
    }
  });
}
