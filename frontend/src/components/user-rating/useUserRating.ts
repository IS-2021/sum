import { computed } from 'vue';
import { computeUserRating, formatUserRating } from '@/components/user-rating/userRating';

export function useUserRating(likes?: number, dislikes?: number) {
  const totalRatings = computed(() => (likes || 0) + (dislikes || 0));
  const ratingPercentageValue = computed(() => computeUserRating(likes, dislikes));
  const ratingPercentage = computed(() => formatUserRating(ratingPercentageValue.value));

  return {
    totalRatings,
    ratingPercentage,
    ratingPercentageValue,
  };
}
