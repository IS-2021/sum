<script setup lang="ts">
import { CircleDashedIcon, Clock4Icon, CircleIcon, CircleCheckBigIcon } from 'lucide-vue-next';
import { cn } from '@/lib/utils';
import { useBookingCard } from '@/components/(manage)/dashboard/useBookingCard';

interface BookingCardProps {
  mealName: string;
  pickupAt: Date;
}

const props = defineProps<BookingCardProps>();

const { isPending, isPast, remainingTime } = useBookingCard(props.pickupAt);
</script>

<template>
  <div
    :class="
      cn(
        'transition-opacity flex justify-between px-6 py-4 bg-white rounded border border-neutral-300',
        (isPending || isPast) && 'opacity-50',
      )
    "
  >
    <p class="flex items-center gap-5">
      <CircleDashedIcon v-if="isPending" />
      <CircleCheckBigIcon v-else-if="isPast" />
      <CircleIcon v-else />
      Hawaian
    </p>

    <p class="flex gap-2">
      <span class="font-semibold">{{ remainingTime }}</span>
      <Clock4Icon />
    </p>
  </div>
</template>
