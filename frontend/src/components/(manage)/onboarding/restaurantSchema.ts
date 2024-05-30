import { z } from 'zod';
import type { HoursDTO, RestaurantInputDTO } from '@/lib/api-model';

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
  addressId: z.string(),
  city: z.string(),
  country: z.string(),
  latitude: z.number(),
  longitude: z.number(),
  number: z.string(),
  postalCode: z.string(),
  street: z.string(),
});

export const restaurantSchema = z.object({
  ownerId: z.string().uuid(),
  details: restaurantDetailsSchema,
  hours: restaurantHoursSchema,
  address: addressSchema,
});

export type RestaurantSchema = z.infer<typeof restaurantSchema>;

export const restaurantSchemaDefaults: RestaurantSchema = {
  ownerId: '',
  details: {
    name: '',
    phoneNumber: '',
  },
  hours: {
    monday: ['', ''],
    tuesday: ['', ''],
    thursday: ['', ''],
    wednesday: ['', ''],
    friday: ['', ''],
    saturday: ['', ''],
    sunday: ['', ''],
  },
  address: {
    addressId: '',
    number: '',
    street: '',
    postalCode: '',
    city: '',
    country: '',
    latitude: 0,
    longitude: 0,
  },
};

export function mapRestaurantDataToDTO(formValues: RestaurantSchema): RestaurantInputDTO {
  const sanitizeHours = (hours: string[]) => {
    return hours.filter((hour) => hour !== '');
  };
  const mappedHours: HoursDTO = Object.entries(formValues.hours).reduce((acc, [key, value]) => {
    acc[key as keyof HoursDTO] = sanitizeHours(value);
    return acc;
  }, {} as HoursDTO);

  return {
    name: formValues.details.name,
    phoneNumber: formValues.details.phoneNumber,
    hours: mappedHours,
    userId: formValues.ownerId,
    addressInputDTO: formValues.address,
  };
}
