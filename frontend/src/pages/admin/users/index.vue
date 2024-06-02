<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetAdminUsers } from '@/lib/api/admin-users/admin-users';
import { computed } from 'vue';
import UserCard from '@/components/(admin)/users/UserCard.vue';
import SearchInput from '@/components/(admin)/common/SearchInput.vue';
import UserStatusSelect from '@/components/(admin)/users/UserStatusSelect.vue';
import type { UserDTO } from '@/lib/api-model';

useHead({
  title: 'Admin - Users',
});

const { data } = useGetAdminUsers();
const users = computed(() => data.value?.data ?? []);

const searchText = defineModel<string>('searchText', {
  type: String,
  default: '',
});
const statusFilter = defineModel<string>('statusFilter', {
  default: 'all',
});

const filteredUsers = computed(() => {
  const filteredByProperty = users.value.filter((restaurant: UserDTO) => {
    const fullText = `${restaurant.firstName} ${restaurant.secondName} ${restaurant.username} ${restaurant.phoneNumber}`;
    return fullText.toLowerCase().includes(searchText.value.toLowerCase());
  });

  if (statusFilter.value === 'all') {
    return filteredByProperty;
  }

  return filteredByProperty.filter(
    (user: UserDTO) => user.blocked === (statusFilter.value === 'Banned'),
  );
});
</script>

<template>
  <div class="mb-10 max-w-screen-md">
    <div
      class="mb-10 flex max-w-screen-xl flex-col justify-between gap-2 lg:flex-row lg:items-center"
    >
      <h1 class="text-2xl font-semibold tracking-tight">Users</h1>

      <div class="flex flex-wrap gap-2 lg:flex-nowrap">
        <SearchInput v-model="searchText" />
        <UserStatusSelect @update:modelValue="(val) => (statusFilter = val)" />
      </div>
    </div>

    <ul v-if="filteredUsers.length > 0" class="space-y-2">
      <li v-for="user in filteredUsers" :key="user.id">
        <UserCard :user="user" />
      </li>
    </ul>
    <p v-else>No users found</p>
  </div>
</template>
