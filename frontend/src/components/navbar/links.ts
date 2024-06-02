import type { AppRoutes } from '@/lib/router';

export const navbarLinks: { name: string; to: AppRoutes }[] = [
  { name: 'Home', to: '/' },
  { name: 'Restaurants', to: '/restaurants' },
  { name: 'Favourites', to: '/favourites' },
  { name: 'All Bookings', to: '/bookings' },
];
