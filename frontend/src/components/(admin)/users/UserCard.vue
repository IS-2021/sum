<script setup lang="ts">
import type { UserDTO } from '@/lib/api-model';
import UserStatusIcon from '@/components/(admin)/users/UserStatusIcon.vue';
import { UserRoundIcon, PhoneIcon } from 'lucide-vue-next';

interface UserCardProps {
  user: UserDTO;
}

defineProps<UserCardProps>();
</script>

<template>
  <article class="flex gap-4 rounded border border-neutral-300 bg-white px-6 py-4">
    <UserStatusIcon :is-banned="user.isBlocked" class="mt-1.5 inline-block h-5 w-5" />
    <div class="flex-grow">
      <header class="flex justify-between">
        <h2 class="mb-1 text-lg font-semibold tracking-tight">
          {{ user.firstName }} {{ user.secondName }}
        </h2>
        <p v-if="user.role === 'ROLE_RESTAURANT'" class="text-primary">Restaurant</p>
      </header>

      <p class="mb-1 text-neutral-500">
        <UserRoundIcon class="mr-2 inline-block h-4 w-4" /> {{ user.username }}
      </p>
      <p class="text-neutral-500">
        <PhoneIcon class="mr-2 inline-block h-4 w-4" /> {{ user.phoneNumber }}
      </p>
    </div>
  </article>
</template>
