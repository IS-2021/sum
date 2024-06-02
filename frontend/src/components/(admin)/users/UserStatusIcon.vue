<script setup lang="ts">
import { BanIcon, CircleIcon } from 'lucide-vue-next';

interface UserStatusIconProps {
  isBanned: boolean;
}

defineProps<UserStatusIconProps>();
</script>

<template>
  <BanIcon v-if="isBanned" class="text-red-500" />
  <CircleIcon v-else class="fill-secondary text-secondary" />
</template>
