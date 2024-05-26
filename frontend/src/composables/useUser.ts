import { computed } from 'vue';
import { useGetUsersMe } from '@/lib/api/users/users';
import { postLogout } from '@/lib/api/auth/auth';
import { toast } from 'vue-sonner';

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

    if (user.value?.role === 'ROLE_USER') {
      return !!user.value?.address;
    }

    return true;
  });

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
  };
}

export type UseUser = ReturnType<typeof useUser>;
