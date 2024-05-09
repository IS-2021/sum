export function computeUserRating(likes?: number, dislikes?: number) {
  if (likes === undefined || dislikes === undefined) {
    console.error('likes and dislikes must be defined - received undefined');
    return 0;
  }

  const total = likes + dislikes;
  if (total === 0) {
    return 0;
  }

  return likes / total;
}

export function formatUserRating(rating: number) {
  return `${Math.floor(rating * 100)}%`;
}
