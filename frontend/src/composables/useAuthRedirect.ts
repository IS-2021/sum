import { useUser } from '@/composables/useUser';
import { computed, watchEffect } from 'vue';
import { useRouter } from 'vue-router/auto';
import type { AppRoutes } from '@/lib/router';

interface AuthRedirectOptions {
  redirectTo: AppRoutes;
  requireAuth?: boolean;
}

/**
 * Redirects the user to a specific route based on their authentication status.
 * By default, it will redirect only if the user is signed in.
 * @param redirectTo The route to redirect to
 * @param requireAuth If true, the user must be signed in for redirect to work
 */
export function useAuthRedirect({ redirectTo, requireAuth = true }: AuthRedirectOptions) {
  const { isSignedIn, isLoaded } = useUser();
  const router = useRouter();

  const shouldRedirect = computed(() => (requireAuth ? isSignedIn.value : !isSignedIn.value));

  watchEffect(async () => {
    if (!isLoaded.value) {
      return;
    }

    if (shouldRedirect.value) {
      await router.push(redirectTo);
    }
  });
}
