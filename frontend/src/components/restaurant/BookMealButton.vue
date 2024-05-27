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
import { postBookings } from '@/lib/api/bookings/bookings';
import { ref } from 'vue';

const props = defineProps<{
  mealId: Uuid;
  userId: Uuid;
}>();

let bookingLink = ref('');

async function bookChosenMeal() {
  const booking = await postBookings({ mealId: props.mealId, userId: props.userId });
  const bookingId = booking.data.id;
  bookingLink.value = `/booking/${bookingId}`;
}
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button class="w-1/2">Book</Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="pb-4">Do you want to book this meal?</DialogTitle>
      </DialogHeader>

      <DialogFooter class="sm:justify-start">
        <DialogClose as-child>
          <RouterLink :to="bookingLink">
            <Button type="button" variant="secondary" @click="bookChosenMeal()"> Book meal </Button>
          </RouterLink>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
