<script setup lang="ts">
import {
  CircleDashedIcon,
  Clock4Icon,
  CircleIcon,
  CircleCheckBigIcon,
  CircleXIcon,
  CircleSlash2Icon,
} from 'lucide-vue-next';
import { cn } from '@/lib/utils';
import { useBookingCard } from '@/components/(manage)/dashboard/useBookingCard';
import type { BookingDTO } from '@/lib/api-model';
import { Button } from '@/components/ui/button';
import { useGetUsersId } from '@/lib/api/users/users';
import { computed, ref, unref } from 'vue';
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import ReportUserComponent from '@/components/(manage)/bookings/ReportUserComponent.vue';
import { useUser } from '@/composables/useUser';

interface BookingCardProps {
  booking: BookingDTO;
}

const props = defineProps<BookingCardProps>();

const { user } = useUser();
const buttonMessage = ref('Report user');

const { data } = useGetUsersId(props.booking.userId);
const client = computed(() => unref(data)?.data);

const emits = defineEmits<{
  (e: 'onBookingAccept', booking: BookingDTO): void;
}>();

const { isPending, remainingTime } = useBookingCard(
  new Date(
    props.booking.status === 'Active'
      ? props.booking.deadlinePickUpTimestamp
      : props.booking.pickedUpTimestamp,
  ),
);
</script>

<template>
  <div
    :class="
      cn(
        'transition-opacity px-6 py-4 bg-white rounded border border-neutral-300',
        (isPending || booking.status !== 'Active') && 'opacity-50',
      )
    "
  >
    <div class="flex items-center justify-between mb-4">
      <p class="flex items-center gap-5">
        <Dialog>
          <DialogTrigger as-child>
            <Button size="icon" variant="ghost" :disabled="booking.status !== 'Active'">
              <CircleDashedIcon v-if="isPending" />
              <CircleCheckBigIcon v-else-if="booking.status === 'PickedUp'" />
              <CircleXIcon v-else-if="booking.status === 'Cancelled'" />
              <CircleIcon v-else-if="booking.status === 'Active'" />
              <CircleSlash2Icon v-else-if="booking.status === 'OutOfDate'" />
            </Button>
          </DialogTrigger>
          <DialogContent class="sm:max-w-[425px]">
            <DialogHeader>
              <DialogTitle>Do you want to mark this meal as picked up?</DialogTitle>
            </DialogHeader>
            <DialogFooter class="w-full flex sm:justify-start">
              <Button @click="emits('onBookingAccept', booking)"> Submit </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
        {{ booking.meal.name }}
      </p>

      <p v-if="booking.status === 'Active' || booking.status === 'PickedUp'" class="flex gap-2">
        <span class="font-semibold">{{ remainingTime }}</span>
        <Clock4Icon />
      </p>
    </div>
    <div v-if="client" class="mb-8">
      <p>Client: {{ client.firstName }} {{ client.secondName }}</p>
      <p>Phone: {{ client.phoneNumber }}</p>
    </div>

    <ReportUserComponent
      v-if="user"
      :restaurantId="user.id"
      :userId="booking.userId"
      :button-message="buttonMessage"
      :booking-status="booking.status"
    />
  </div>
</template>
