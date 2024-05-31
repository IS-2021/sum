<script setup lang="ts">
import { putBookingsId } from '@/lib/api/bookings/bookings';
import type { BookingDTO } from '@/lib/api-model';

import { toast } from 'vue-sonner';
import Button from '../ui/button/Button.vue';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';

const props = defineProps<{
  booking: BookingDTO;
}>();

async function cancelBooking() {
  const res = await putBookingsId(props.booking.bookingId, {
    bookingId: props.booking.bookingId,
    deadlinePickUpTimestamp: props.booking.deadlinePickUpTimestamp,
    meal: props.booking.meal,
    orderedTimestamp: props.booking.orderedTimestamp,
    pickedUpTimestamp: props.booking.pickedUpTimestamp,
    restaurant: props.booking.restaurant,
    status: 'Cancelled',
    userId: props.booking.userId,
  });

  if (res.status === 200) {
    location.reload();
    toast.success('Booking cancelled successfully!');
  }
}
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button variant="ghost"> Cancel Booking </Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Are you want to cancel this booking?</DialogTitle>
      </DialogHeader>
      <DialogFooter class="w-full flex sm:justify-start">
        <Button v-if="props.booking.status === 'Active'" @click="cancelBooking" class="mt-4"
          >Confirm</Button
        >
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
