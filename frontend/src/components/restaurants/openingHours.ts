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
