import { onMounted, onUnmounted, ref } from 'vue';
import {
  addMinutes,
  differenceInSeconds,
  formatDistanceToNow,
  formatDistanceToNowStrict,
  isAfter,
  isBefore,
} from 'date-fns';

/**
 * Order is considered pending if it's more than 45 minutes away.
 */
const getIsPending = (date: Date) => {
  return isAfter(date, addMinutes(Date.now(), 45));
};

const getIsPast = (date: Date) => {
  return isBefore(date, Date.now());
};

const getRemainingTime = (date: Date) => {
  const secondsDiff = differenceInSeconds(date, Date.now());

  if (secondsDiff >= 0) {
    return formatDistanceToNowStrict(date, { addSuffix: true });
  }

  return formatDistanceToNow(date, { addSuffix: true });
};

export function useBookingCard(pickupAt: Date) {
  const isPending = ref(getIsPending(pickupAt));
  const remainingTime = ref(getRemainingTime(pickupAt));
  const isPast = ref(getIsPast(pickupAt));

  onMounted(() => {
    const interval = setInterval(() => {
      remainingTime.value = getRemainingTime(pickupAt);
      isPending.value = getIsPending(pickupAt);
      isPast.value = getIsPast(pickupAt);
    }, 1000);

    onUnmounted(() => {
      clearInterval(interval);
    });
  });

  return { isPending, isPast, remainingTime };
}
