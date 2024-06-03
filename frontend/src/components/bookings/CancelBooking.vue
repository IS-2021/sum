<script setup lang="ts">
import { deleteBookingsId, putBookingsId } from '@/lib/api/bookings/bookings';
import type { BookingDTO } from '@/lib/api-model';

import { toast } from 'vue-sonner';
import Button from '../ui/button/Button.vue';
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';

const props = defineProps<{
  booking: BookingDTO;
}>();

async function cancelBooking() {
  const res = await deleteBookingsId(props.booking.bookingId);

  if (res.status === 200) {
    toast.success('Booking cancelled successfully!');
    location.reload();
  }
}
</script>

<template>
  <Dialog v-if="booking.status === 'Active'">
    <DialogTrigger as-child>
      <Button variant="ghost"> Cancel Booking </Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Do you want to cancel this booking?</DialogTitle>
      </DialogHeader>
      <DialogFooter class="w-full flex sm:justify-start">
        <Button v-if="props.booking.status === 'Active'" @click="cancelBooking" class="mt-4"
          >Confirm</Button
        >
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
