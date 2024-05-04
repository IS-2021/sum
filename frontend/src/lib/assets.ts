import fallbackImgUrl from '@/assets/images/restaurant-image-1.jpg';

export function getImageUrl(imgName?: string) {
  if (!imgName) {
    return fallbackImgUrl;
  }

  return `${import.meta.env.VITE_IMG_URL}${imgName}`;
}
