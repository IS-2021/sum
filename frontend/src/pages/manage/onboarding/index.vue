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

useHead({
  title: 'Complete restaurant profile',
});

const { restaurant, isProfileComplete, signOut } = useRestaurantUser();

const { current, goToPrevious, goToNext, isCurrent } = useStepper([
  // 'greeting',
  'details',
  'hours',
  'location',
]);

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
  location: {
    address: 'Address',
    city: 'City',
    state: 'State',
    zip: 'Zip',
  },
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
    <div class=""></div>
    <div class="space-y-5 p-10 md:h-svh">
      <h1>Restaurant form</h1>

      <form @submit="onSubmit" class="space-y-4">
        <!-- Details -->
        <div :class="cn('hidden', isCurrent('details') && 'block')">
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

        <!--        <Button type="submit"> Submit </Button>-->
      </form>

      <div class="flex gap-2">
        <Button @click="goToPrevious"> Previous </Button>
        <Button @click="goToNext"> Next </Button>
      </div>
    </div>
  </div>
</template>
