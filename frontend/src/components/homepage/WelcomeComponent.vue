<script setup lang="ts">
import type { UserMeDTO } from '@/lib/api-model';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import { computed, unref } from 'vue';

import Logo from '../Logo.vue';
import { cn } from '@/lib/utils';

const props = defineProps<{
  user: UserMeDTO;
}>();

const { data } = useGetBookings({ userId: props.user.id });
const bookings = computed(() => unref(data)?.data ?? []);

const completedBookingsCount = computed(
  () => bookings.value.filter((booking) => booking.status === 'PickedUp').length,
);
</script>

<template>
  <div class="mb-8 rounded-md bg-secondary p-8 py-6">
    <div class="mb-2.5 flex flex-col items-start justify-center rounded-md">
      <p v-if="user" class="text-2xl font-bold">Welcome, {{ props.user.firstName }}!</p>
    </div>
    <div
      :class="
        cn(
          'flex flex-wrap items-center gap-3',
          completedBookingsCount > 0 ? 'justify-between' : 'justify-end',
        )
      "
    >
      <p class="text-lg" v-if="completedBookingsCount > 0">
        You saved {{ completedBookingsCount }} meals so far!
      </p>

      <div class="flex items-center gap-2 self-end">
        <p>FoodGood team</p>
        <Logo class="size-5 fill-accent" />
      </div>
    </div>
  </div>
</template>
