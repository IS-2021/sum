import { z } from 'zod';

export const restaurantDetailsSchema = z.object({
  name: z.string().min(1, "Name can't be empty").max(50, 'Name is too long'),
  phoneNumber: z.string().min(9, 'Phone number is too short').max(11, 'Phone number is too long'),
});

const hoursSchema = z
  .string()
  .refine((val) => {
    if (val === '') return true;
    return /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/.test(val);
  }, 'Invalid time format')
  .array()
  .max(2);

export const restaurantHoursSchema = z.object({
  monday: hoursSchema,
  tuesday: hoursSchema,
  thursday: hoursSchema,
  wednesday: hoursSchema,
  friday: hoursSchema,
  saturday: hoursSchema,
  sunday: hoursSchema,
});

export const addressSchema = z.object({
  city: z.string(),
  country: z.string(),
  latitude: z.number(),
  longitude: z.number(),
  number: z.string(),
  postalCode: z.string(),
  street: z.string(),
});

export const restaurantSchema = z.object({
  details: restaurantDetailsSchema,
  hours: restaurantHoursSchema,
});

export type RestaurantSchema = z.infer<typeof restaurantSchema>;
