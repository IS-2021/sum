<script setup lang="ts">
import { BanIcon, CircleIcon } from 'lucide-vue-next';

interface UserStatusIconProps {
  isBanned: boolean;
}

defineProps<UserStatusIconProps>();
</script>

<template>
  <BanIcon v-if="isBanned" />
  <CircleIcon v-else />
</template>
