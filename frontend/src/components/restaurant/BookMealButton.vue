<script setup lang="ts">
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import type { Uuid } from '@/lib/api-model';
import { postBookings, useGetBookingsActive } from '@/lib/api/bookings/bookings';
import { useRouter } from 'vue-router/auto';
import { computed, unref } from 'vue';

const props = defineProps<{
  mealId: Uuid;
  userId: Uuid;
}>();

const { data } = useGetBookingsActive({ userId: props.userId });
const activeBooking = computed(() => unref(data));

const router = useRouter();

async function bookChosenMeal() {
  const booking = await postBookings({ mealId: props.mealId, userId: props.userId });

  if (booking.status === 200) {
    await router.push(`/activeBooking`);
  }
}
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button v-if="activeBooking" :disabled="activeBooking.status === 200" class="w-1/2"
        >Book</Button
      >
    </DialogTrigger>
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="pb-4">Do you want to book this meal?</DialogTitle>
      </DialogHeader>

      <DialogFooter class="sm:justify-start">
        <DialogClose as-child>
          <Button type="button" variant="secondary" @click="bookChosenMeal"> Book meal </Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
