import type { HoursDTO } from '@/lib/api-model';

export const openingHoursToString = (hours?: string[]) => {
  if (hours?.length != 2) {
    return 'Closed';
  }

  if (hours) {
    const [openingHours, closingHours] = hours;
    return `${openingHours} - ${closingHours}`;
  }

  return 'Closed';
};

export const isCurrentDay = (day: string) => {
  return day === new Date().toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase();
};

export const findCurrentDayHours = (hours: HoursDTO) => {
  const currentDayHours = Object.entries(hours).find(([day]) => isCurrentDay(day));
  return currentDayHours ? currentDayHours[1] : [];
};

export const sortedDaysOfWeek = (hours: HoursDTO) => {
  return {
    monday: hours.monday,
    tuesday: hours.tuesday,
    wednesday: hours.wednesday,
    thursday: hours.thursday,
    friday: hours.friday,
    saturday: hours.saturday,
    sunday: hours.sunday,
  };
};
