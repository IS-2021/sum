<route lang="yaml">
meta:
  layout: empty
</route>

<script setup lang="ts">
import AddressPickerPage from '@/components/maps/address-picker/AddressPickerPage.vue';
import { useHead } from '@unhead/vue';
import { postUsersUserIdAddress } from '@/lib/api/users/users';
import { useUser } from '@/composables/useUser';
import { useRouter } from 'vue-router/auto';
import type { AddressDTO } from '@/lib/api-model';
import { Button } from '@/components/ui/button';
import { LogOutIcon } from 'lucide-vue-next';
import Logo from '@/components/Logo.vue';

useHead({
  title: 'Complete your profile',
});

const { user, signOut } = useUser();
const router = useRouter();

async function saveUserAddress(address: AddressDTO) {
  if (!user.value || !address) {
    return;
  }

  const res = await postUsersUserIdAddress(user.value.id, {
    placeId: address.addressId,
  });
  if (res.status === 200) {
    await router.push('/');
  }
}
</script>

<template>
  <AddressPickerPage @save:address="saveUserAddress">
    <template v-slot:page-header>
      <div class="hidden lg:grid justify-end">
        <Button variant="ghost" class="text-neutral-500" @click="signOut">
          <LogOutIcon class="h-4 w-4 mr-2" /> Sign out
        </Button>
      </div>
    </template>

    <template v-slot:content-header>
      <div class="flex justify-between lg:block">
        <Logo class="mb-8" />

        <Button variant="ghost" class="text-neutral-500 lg:hidden" @click="signOut">
          <LogOutIcon class="h-4 w-4 mr-2" /> Sign out
        </Button>
      </div>

      <h1 class="text-2xl font-bold mb-2">Welcome to FoodGood</h1>

      <p class="mb-8 text-neutral-700 max-w-prose">
        Before you continue, you need to set your location. We’ll use it to show restaurants near
        you. You can always change it latter in the settings.
      </p>
    </template>
  </AddressPickerPage>
</template>
