import { useUser } from '@/composables/useUser';
import { computed, watch, watchEffect } from 'vue';
import { useRouter } from 'vue-router/auto';
import type { AppRouteNames } from '@/lib/router';
import { Role } from '@/lib/api-model';
import { useRestaurantUser } from '@/composables/useRestaurantUser';

const defaultRoutesByRole: Record<Role | 'GUEST', AppRouteNames> = {
  GUEST: '/sign-in/',
  ROLE_USER: '/',
  ROLE_RESTAURANT: '/manage/',
  ROLE_ADMIN: '/admin/',
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
    '/manage/meals/add',
    '/manage/meals/edit/[id]',
    '/manage/onboarding/',
    '/manage/reports/',
    '/manage/settings/',
  ],
  ROLE_ADMIN: [
    '/admin/',
    '/admin/restaurants/',
    '/admin/users/',
    '/admin/restaurants/[id]',
    '/admin/users/[id]',
  ],
};

/**
 * Redirects user based on their authentication status.
 * Regular users without a complete profile will be redirected to the onboarding page.
 * Guest
 */
export function useAuthRedirect() {
  const router = useRouter();
  const { user, isLoaded, isSignedIn, isProfileComplete } = useUser();
  const { isProfileComplete: isRestaurantProfileComplete } = useRestaurantUser();

  const userRole = computed(() => user.value?.role ?? 'GUEST');

  const currentRoute = computed(() => router.currentRoute.value.name);
  const allowedRoutes = computed(() => roleBasedRoutes[user.value?.role ?? 'GUEST']);
  const isCurrentRouteAllowed = computed(() => allowedRoutes.value.includes(currentRoute.value));
  const defaultRoute = computed(() => defaultRoutesByRole[user.value?.role ?? 'GUEST']);

  async function profileCompletionGuard() {
    if (!isLoaded.value || !isSignedIn.value || userRole.value === 'GUEST') {
      return;
    }

    if (userRole.value === 'ROLE_USER') {
      switch (currentRoute.value) {
        case '/onboarding/':
          if (isProfileComplete.value) {
            await router.push('/');
          }
          break;
        default:
          if (!isProfileComplete.value) {
            await router.push('/onboarding/');
          }
          break;
      }
    }

    if (userRole.value === 'ROLE_RESTAURANT') {
      switch (currentRoute.value) {
        case '/manage/onboarding/':
          if (isRestaurantProfileComplete.value) {
            await router.push('/manage/');
            console.log('redirected', isRestaurantProfileComplete.value);
          }
          break;
        default:
          if (!isRestaurantProfileComplete.value) {
            await router.push('/manage/onboarding/');
            console.log('redirected - onboarding', isRestaurantProfileComplete.value);
          }
          break;
      }
    }
  }

  watch([isLoaded, currentRoute, isProfileComplete], profileCompletionGuard);

  watchEffect(async () => {
    if (!isLoaded.value || !currentRoute.value) {
      return;
    }

    if (isSignedIn.value && !isCurrentRouteAllowed.value) {
      await router.replace(defaultRoute.value);
    } else if (!isSignedIn.value && !isCurrentRouteAllowed.value) {
      await router.replace('/sign-in');
    }
  });
}
