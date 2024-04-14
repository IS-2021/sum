<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import RegularUserForm from '@/components/sign-up/SignUpForm.vue';

useHead({
  title: 'Sign-Up',
});
</script>

<template>
  <div class="border border-neutral-900 grid items-center flex-grow">
    <div class="mx-auto border border-neutral-800 p-8 rounded">
      <h1 class="text-xl text-center font-bold mb-8">Sign-Up</h1>
      <Tabs default-value="user" class="w-[400px]">
        <TabsList class="grid w-full gap-4 grid-cols-2">
          <TabsTrigger value="user">Regular user</TabsTrigger>
          <TabsTrigger value="restaurant">Restaurant</TabsTrigger>
        </TabsList>
        <TabsContent value="user">
          <RegularUserForm role="user" />
        </TabsContent>
        <TabsContent value="restaurant">
          <RegularUserForm role="restaurant" />
        </TabsContent>
      </Tabs>

      <p class="w-full text-sm text-center mt-4">
        Already have an account?
        <RouterLink to="/sign-in" class="underline">Sign in</RouterLink>
      </p>
    </div>
  </div>
</template>
