<route lang="yaml">
meta:
  layout: settings
</route>

<script setup lang="ts">
import AddressPickerPage from '@/components/maps/address-picker/AddressPickerPage.vue';
import { useHead } from '@unhead/vue';
import { postUsersUserIdAddress } from '@/lib/api/users/users';
import { useUser } from '@/composables/useUser';
import { useRouter } from 'vue-router/auto';
import type { AddressDTO } from '@/lib/api-model';

useHead({
  title: 'Change your address',
});

const { user } = useUser();
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
    <template v-slot:content-header>
      <h1 class="text-2xl font-bold mb-8">Change your address</h1>
    </template>
  </AddressPickerPage>
</template>
