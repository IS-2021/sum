import { computed } from 'vue';
import { computeUserRating } from '@/components/user-rating/userRating';

export function useUserRating(likes?: number, dislikes?: number) {
  const totalRatings = computed(() => (likes || 0) + (dislikes || 0));
  const ratingPercentage = computed(() => computeUserRating(likes, dislikes) * 100);

  return {
    totalRatings,
    ratingPercentage,
  };
}
