<script setup lang="ts">
import { useRestaurantUser } from '@/composables/useRestaurantUser';
import { useHead } from '@unhead/vue';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { watchEffect } from 'vue';
import { toTypedSchema } from '@vee-validate/zod';
import { restaurantSchema } from '@/components/(manage)/onboarding/restaurantSchema';
import { useForm } from 'vee-validate';
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { useStepper } from '@vueuse/core';
import { cn } from '@/lib/utils';
import { LogOutIcon, ChevronRightIcon, ChevronLeftIcon, ClockIcon } from 'lucide-vue-next';
import Logo from '@/components/Logo.vue';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';

useHead({
  title: 'Complete restaurant profile',
});

const { restaurant, isProfileComplete, signOut } = useRestaurantUser();

const { current, goToPrevious, goToNext, isCurrent, isFirst, isLast } = useStepper({
  greeting: {
    label: 'Welcome to FoodGood',
    description:
      'Before you continue, you need to finish setting up your restaurant profile. After your restaurant profile is verified, you can start your food saving journey.',
  },
  details: {
    label: 'Restaurant details',
    description: 'First, we need some basic information about your restaurant.',
  },
  hours: {
    label: 'Opening hours',
    description: 'Next, let us know when your restaurant is open.',
  },
  location: {
    label: 'Restaurant location',
    description: 'Finally, tell us where your restaurant is located.',
  },
});

const formFields = {
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

const formSchema = toTypedSchema(restaurantSchema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    details: {
      name: 'Laxmi',
      phoneNumber: '123456789',
    },
    hours: {
      monday: ['10:00', '20:00'],
      tuesday: ['10:00', '20:00'],
      thursday: ['10:00', '20:00'],
      wednesday: ['10:00', '20:00'],
      friday: ['10:00', '20:00'],
      saturday: ['10:00', '20:00'],
      sunday: ['10:00', '20:00'],
    },
  },
});

watchEffect(() => {
  console.log('Form values:', form.errors.value);
  console.log('Formbag values:', form.errorBag.value);
});

const onSubmit = form.handleSubmit((values) => {
  console.log('Form submitted!', values);
});
</script>

<template>
  <div class="grid min-h-svh grid-cols-2">
    <div class="border-r border-neutral-300">
      <img
        src="@/assets/images/chris-liverani-oCsaxvGCehM-unsplash-crop.jpg"
        alt="FoodGood background image"
        class="aria-hidden h-svh w-full object-cover object-right-top opacity-70"
      />
    </div>

    <div class="p-10 md:h-svh">
      <div class="flex justify-between">
        <Logo class="mb-8" />

        <Button variant="ghost" class="text-neutral-500" @click="signOut">
          <LogOutIcon class="mr-2 h-4 w-4" /> Sign out
        </Button>
      </div>

      <h1 class="mb-2 text-2xl font-bold">{{ current.label }}</h1>

      <p class="mb-8 max-w-prose text-neutral-700">{{ current.description }}</p>

      <form @submit="onSubmit">
        <!-- Details -->
        <div :class="cn('hidden space-y-4', isCurrent('details') && 'block')">
          <FormField
            v-for="{ label, fieldName } in formFields.details"
            :key="fieldName"
            v-slot="{ componentField }"
            :name="fieldName"
          >
            <FormItem>
              <FormLabel>{{ label }}</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>

        <!-- Hours -->
        <div :class="cn('hidden', isCurrent('hours') && 'block')">
          <Alert class="mb-3 bg-secondary/25">
            <ClockIcon class="h-4 w-4" />
            <AlertTitle>How to fill the form</AlertTitle>
            <AlertDescription>
              <ul class="list-disc">
                <li>Use HH:MM 24-hour format,</li>
                <li>If restaurant is closed on a given day, leave both fields empty.</li>
              </ul>
            </AlertDescription>
          </Alert>
        </div>
        <div :class="cn('hidden max-w-80', isCurrent('hours') && 'block')">
          <div class="grid grid-cols-[max-content,1fr,1fr] gap-2 gap-x-4">
            <p class="col-start-2 text-sm font-medium">Open</p>
            <p class="text-sm font-medium">Close</p>

            <template v-for="{ label, fieldName } in formFields.hours" :key="fieldName">
              <p aria-hidden="true" class="h-fit self-center">
                {{ label }}
              </p>

              <FormField v-slot="{ componentField }" :name="`${fieldName}[0]`">
                <FormItem class="space-y-0">
                  <FormLabel class="sr-only">Open</FormLabel>
                  <FormControl>
                    <Input type="text" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField v-slot="{ componentField }" :name="`${fieldName}[1]`">
                <FormItem class="space-y-0">
                  <FormLabel class="sr-only">Close</FormLabel>
                  <FormControl>
                    <Input type="text" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </template>
          </div>
        </div>

        <div class="mt-8 flex gap-2">
          <Button type="button" :disabled="isFirst" variant="outline" @click="goToPrevious">
            <ChevronLeftIcon class="mr-2 h-4 w-4" /> Back
          </Button>
          <Button type="button" @click="goToNext" v-if="!isLast">
            Continue <ChevronRightIcon class="ml-2 h-4 w-4" />
          </Button>
          <Button v-else type="submit">Save</Button>
        </div>
      </form>
    </div>
  </div>
</template>
