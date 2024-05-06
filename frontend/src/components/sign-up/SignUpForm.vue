<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod';
import { z } from 'zod';
import { useForm } from 'vee-validate';
import { postAuthRegister } from '@/lib/api/auth/auth';
import {
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Role, type ValidationFailed422Response } from '@/lib/api-model';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { AlertCircleIcon } from 'lucide-vue-next';
import { ref } from 'vue';
import { useRouter } from 'vue-router/auto';

const router = useRouter();

const formSchema = toTypedSchema(
  z.object({
    role: z.enum([Role.ROLE_RESTAURANT, Role.ROLE_USER]).default(Role.ROLE_USER),
    firstName: z.string().min(1),
    secondName: z.string().min(1),
    email: z.string().email(),
    username: z.string().min(3),
    phoneNumber: z.string().min(1),
    password: z.string().min(6),
  }),
);

const form = useForm({
  validationSchema: formSchema,
});
const errorMessage = ref('');

const onSubmit = form.handleSubmit(async (formData) => {
  const res = await postAuthRegister({
    ...formData,
  });

  if (res.status === 201) {
    errorMessage.value = '';
    await router.push('/sign-in');
  } else if (res.status === 400) {
    const { message } = res.data as unknown as ValidationFailed422Response;

    errorMessage.value = message;
  }
});

const isValid = form.meta.value.valid;
</script>

<template>
  <Alert variant="destructive" v-if="errorMessage" class="mb-4">
    <AlertCircleIcon class="h-4 w-4" />
    <AlertTitle>There's an error</AlertTitle>
    <AlertDescription>
      {{ errorMessage }}
    </AlertDescription>
  </Alert>

  <form @submit="onSubmit" class="space-y-4">
    <FormField v-slot="{ componentField }" name="role">
      <FormItem>
        <FormLabel>Who are you?</FormLabel>
        <FormControl>
          <Tabs v-bind="componentField" default-value="ROLE_USER">
            <TabsList class="grid gap-4 grid-cols-2">
              <TabsTrigger value="ROLE_USER">A regular user</TabsTrigger>
              <TabsTrigger value="ROLE_RESTAURANT">A restaurant</TabsTrigger>
            </TabsList>
          </Tabs>
        </FormControl>
        <FormDescription class="sr-only">Your first name</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>

    <div class="flex gap-4">
      <FormField v-slot="{ componentField }" name="firstName">
        <FormItem class="flex-grow">
          <FormLabel>First name</FormLabel>
          <FormControl>
            <Input type="text" placeholder="John" v-bind="componentField" />
          </FormControl>
          <FormDescription class="sr-only">Your first name</FormDescription>
          <FormMessage />
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" name="secondName">
        <FormItem class="flex-grow">
          <FormLabel>Second name</FormLabel>
          <FormControl>
            <Input type="text" placeholder="Cena" v-bind="componentField" />
          </FormControl>
          <FormDescription class="sr-only">Your last name</FormDescription>
          <FormMessage />
        </FormItem>
      </FormField>
    </div>

    <FormField v-slot="{ componentField }" name="email">
      <FormItem>
        <FormLabel>Email</FormLabel>
        <FormControl>
          <Input type="email" placeholder="E-Mail" v-bind="componentField" />
        </FormControl>
        <FormDescription class="sr-only">Your email</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="username">
      <FormItem>
        <FormLabel>Username</FormLabel>
        <FormControl>
          <Input type="text" placeholder="Username" v-bind="componentField" />
        </FormControl>
        <FormDescription class="sr-only">Your username</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="phoneNumber">
      <FormItem>
        <FormLabel>Phone Number</FormLabel>
        <FormControl>
          <div
            class="flex items-center rounded-md border border-neutral-200 dark:border-neutral-800"
          >
            <div
              class="px-2 h-10 grid items-center text-neutral-500 bg-neutral-200 dark:bg-neutral-800"
            >
              +48
            </div>
            <Input
              type="text"
              placeholder="111222333"
              v-bind="componentField"
              class="border-none"
            />
          </div>
        </FormControl>
        <FormDescription class="sr-only">Your phone number</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="password">
      <FormItem>
        <FormLabel>Password</FormLabel>
        <FormControl>
          <Input type="password" placeholder="Password" v-bind="componentField" />
        </FormControl>
        <FormDescription class="sr-only">Your password</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>

    <Button :disabled="!isValid" type="submit" class="mt-8 w-full">Sign up</Button>
  </form>
</template>
