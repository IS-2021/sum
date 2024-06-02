<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetAdminUsers } from '@/lib/api/admin-users/admin-users';
import { computed } from 'vue';
import UserCard from '@/components/(admin)/users/UserCard.vue';

useHead({
  title: 'Admin - Users',
});

const { data } = useGetAdminUsers();
const users = computed(() => data.value?.data ?? []);
</script>

<template>
  <div class="max-w-screen-md">
    <h1 class="mb-10 text-2xl font-semibold tracking-tight">Users</h1>

    <ul v-if="users.length > 0" class="space-y-2">
      <li v-for="user in users" :key="user.id">
        <UserCard :user="user" />
      </li>
    </ul>
    <p v-else>No users found</p>
  </div>
</template>
