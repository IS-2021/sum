import { computed } from 'vue';
import { useGetUsersMe } from '@/lib/api/users/users';
import { postLogout } from '@/lib/api/auth/auth';
import { toast } from 'vue-sonner';
import type { UserMeDTO } from '@/lib/api-model';

export function isUserProfileComplete(user: UserMeDTO) {
  if (user?.role === 'ROLE_USER') {
    return !!user?.address;
  }
  return true;
}

export function useUser() {
  const { isPending, data, refetch } = useGetUsersMe();

  const isSignedIn = computed(() => data.value?.status === 200 ?? false);
  const isLoaded = computed(() => !isPending.value);
  const user = computed(() => data.value?.data);

  const isProfileComplete = computed(() => {
    if (!isSignedIn.value) {
      return true;
    }

    if (!user.value || !isSignedIn.value) {
      return false;
    }

    return isUserProfileComplete(user.value);
  });

  async function invalidateCache() {
    await refetch();
  }

  const signOut = async () => {
    await postLogout();
    await refetch();
    toast.success("Goodbye! You've been signed out.");
  };

  return {
    isSignedIn,
    isLoaded,
    isProfileComplete,
    user,
    signOut,
    invalidateCache,
  };
}

export type UseUser = ReturnType<typeof useUser>;
