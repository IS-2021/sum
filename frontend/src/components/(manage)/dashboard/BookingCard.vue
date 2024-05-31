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
import { computed, unref } from 'vue';

interface BookingCardProps {
  booking: BookingDTO;
}

const props = defineProps<BookingCardProps>();

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
        <Button
          size="icon"
          variant="ghost"
          @click="emits('onBookingAccept', booking)"
          :disabled="booking.status !== 'Active'"
        >
          <CircleDashedIcon v-if="isPending" />
          <CircleCheckBigIcon v-else-if="booking.status === 'PickedUp'" />
          <CircleXIcon v-else-if="booking.status === 'Cancelled'" />
          <CircleIcon v-else-if="booking.status === 'Active'" />
          <CircleSlash2Icon v-else-if="booking.status === 'OutOfDate'" />
        </Button>
        {{ booking.meal.name }}
      </p>

      <p v-if="booking.status === 'Active' || booking.status === 'PickedUp'" class="flex gap-2">
        <span class="font-semibold">{{ remainingTime }}</span>
        <Clock4Icon />
      </p>
    </div>
    <div v-if="client">
      <p>Client: {{ client.firstName }} {{ client.secondName }}</p>
      <p>Phone: {{ client.phoneNumber }}</p>
    </div>
  </div>
</template>
