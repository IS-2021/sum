export const formFields = {
  details: [
    {
      label: 'Restaurant name',
      fieldName: 'details.name',
    },
    {
      label: 'Phone number',
      fieldName: 'details.phoneNumber',
    },
  ],
  hours: [
    {
      label: 'Monday',
      fieldName: 'hours.monday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Tuesday',
      fieldName: 'hours.tuesday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Wednesday',
      fieldName: 'hours.wednesday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Thursday',
      fieldName: 'hours.thursday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Friday',
      fieldName: 'hours.friday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Saturday',
      fieldName: 'hours.saturday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Sunday',
      fieldName: 'hours.sunday',
      fields: ['Open', 'Close'],
    },
  ],
};
