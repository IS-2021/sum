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
import { isCurrentDay } from '@/components/restaurants/openingHours';

interface RestaurantInfoProps {
  hours: HoursDTO;
  address: AddressDTO;
}

const props = defineProps<RestaurantInfoProps>();

const sortedDaysOfWeek: HoursDTO = {
  monday: props.hours.monday,
  tuesday: props.hours.tuesday,
  wednesday: props.hours.wednesday,
  thursday: props.hours.thursday,
  friday: props.hours.friday,
  saturday: props.hours.saturday,
  sunday: props.hours.sunday,
};
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Info @click="console.log(props.hours)" />
    </DialogTrigger>
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Restaurant info</DialogTitle>
        <DialogDescription>
          <p class="mb-2 text-md font-bold">Opening Hours</p>

          <ul>
            <li
              v-for="[day, [openingHours, closingHours]] in Object.entries(sortedDaysOfWeek)"
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
      </DialogHeader>

      <DialogFooter class="sm:justify-start">
        <DialogClose as-child>
          <Button type="button" variant="secondary"> Close </Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
