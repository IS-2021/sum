<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue';
import type { Uuid } from '@/lib/api-model';
import { useGetBookingsActive } from '@/lib/api/bookings/bookings';
import { computed, unref } from 'vue';

const props = defineProps<{
  userId: Uuid;
}>();

const { data } = useGetBookingsActive({ userId: props.userId });
const activeBooking = computed(() => unref(data));
</script>

<template>
  <div v-if="activeBooking">
    <RouterLink v-if="activeBooking.status === 200" to="/activeBooking">
      <Button>Go to active booking</Button>
    </RouterLink>
    <Button v-else disabled>Go to active booking</Button>
  </div>
</template>
