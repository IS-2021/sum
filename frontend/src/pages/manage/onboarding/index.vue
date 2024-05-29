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

useHead({
  title: 'Complete restaurant profile',
});

const { restaurant, isProfileComplete, signOut } = useRestaurantUser();

const formSchema = toTypedSchema(restaurantSchema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    details: {
      name: 'Laxmi',
      phoneNumber: '123456789',
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

        <!-- Hours -->
        <FormField v-slot="{ componentField }" name="hours.monday[0]">
          <FormItem>
            <FormLabel>Monday</FormLabel>
            <FormControl>
              <Input type="text" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="hours.monday[1]">
          <FormItem>
            <FormLabel>Monday</FormLabel>
            <FormControl>
              <Input type="text" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <Button type="submit"> Submit </Button>
      </form>
    </div>
  </div>
</template>
