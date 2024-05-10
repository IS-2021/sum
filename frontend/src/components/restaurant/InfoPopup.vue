<script setup lang="ts">
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Info } from 'lucide-vue-next';
import type { AddressDTO, HoursDTO } from '@/lib/api-model';
import OpeningHoursRow from '@/components/restaurants/OpeningHoursRow.vue';
import { isCurrentDay, sortedDaysOfWeek } from '@/components/restaurants/openingHours';

interface RestaurantInfoProps {
  hours: HoursDTO;
  address: AddressDTO;
  phone: string;
}

const props = defineProps<RestaurantInfoProps>();

const daysOfWeek: HoursDTO = sortedDaysOfWeek(props.hours);
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Info />
    </DialogTrigger>
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="pb-4">Restaurant info</DialogTitle>
        <DialogDescription class="pb-4">
          <p class="mb-2 text-md font-bold">Address</p>

          <div>
            <p>{{ props.address.country }}</p>
            <p>
              {{ props.address.street }} {{ props.address.number }}, {{ props.address.postalCode }}
              {{ props.address.city }}
            </p>
          </div>
        </DialogDescription>
        <DialogDescription class="pb-4">
          <p class="mb-2 text-md font-bold">Opening Hours</p>

          <ul>
            <li
              v-for="[day, [openingHours, closingHours]] in Object.entries(daysOfWeek)"
              :key="day"
            >
              <OpeningHoursRow
                :day="day"
                :opening-hours="openingHours"
                :closing-hours="closingHours"
                :is-current-day="isCurrentDay(day)"
              />
            </li>
          </ul>
        </DialogDescription>
        <DialogDescription class="pb-4">
          <p class="mb-2 text-md font-bold">Phone number</p>

          <p>{{ props.phone }}</p>
        </DialogDescription>
      </DialogHeader>

      <DialogFooter class="sm:justify-start">
        <DialogClose as-child>
          <Button type="button" variant="secondary"> Close </Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
