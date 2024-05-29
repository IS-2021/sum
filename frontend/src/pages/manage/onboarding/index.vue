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
    <div class="grid-rows-5 p-10 md:grid md:h-svh">
      <h1>Restaurant form</h1>
      <form @submit="onSubmit" class="space-y-4">
        <!-- Details -->
        <div :class="cn('hidden', isCurrent('details') && 'block')">
          <FormField v-slot="{ componentField }" name="details.name">
            <FormItem>
              <FormLabel>Restaurant name</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="details.phoneNumber">
            <FormItem>
              <FormLabel>Phone number</FormLabel>
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
            <p class="col-start-2 text-neutral-500">Open</p>
            <p class="text-neutral-500">Close</p>

            <p aria-hidden="true" class="h-fit self-center">Monday</p>

            <FormField v-slot="{ componentField }" name="hours.monday[0]">
              <FormItem class="space-y-0">
                <FormLabel class="sr-only">Open</FormLabel>
                <FormControl>
                  <Input type="text" v-bind="componentField" />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField v-slot="{ componentField }" name="hours.monday[1]">
              <FormItem class="space-y-0">
                <FormLabel class="sr-only">Close</FormLabel>
                <FormControl>
                  <Input type="text" v-bind="componentField" />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>
        </div>

        <Button type="submit"> Submit </Button>
      </form>
    </div>
  </div>
</template>
