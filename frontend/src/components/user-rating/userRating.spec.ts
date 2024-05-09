import { describe, it, expect } from 'vitest';
import { computeUserRating } from '@/components/user-rating/userRating';

describe('userRating', () => {
  it('should return 0 when no ratings are present', () => {
    const likes = 0;
    const dislikes = 0;

    const result = computeUserRating(likes, dislikes);

    expect(result).toBe(0);
  });

  it('should return 0 when ratings are undefined', () => {
    const likes = undefined;
    const dislikes = undefined;

    //@ts-ignore
    const result = computeUserRating(likes, dislikes);

    expect(result).toBe(0);
  });

  it('should return a percentage when there are equal likes and dislikes', () => {
    const likes = 95;
    const dislikes = 5;

    const result = computeUserRating(likes, dislikes);

    expect(result).toBeCloseTo(0.95);
  });
});
