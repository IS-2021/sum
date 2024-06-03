<script setup lang="ts">
import NavLink from '@/components/(manage)/common/NavLink.vue';
import Logo from '@/components/Logo.vue';
import { ChefHatIcon, LogOutIcon, UsersRoundIcon } from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { useUser } from '@/composables/useUser';

const { signOut } = useUser();
</script>

<template>
  <aside class="sticky top-0 flex h-full max-h-svh w-full flex-col px-6 py-5">
    <RouterLink to="/admin/restaurants">
      <Logo class="mb-8" />
    </RouterLink>

    <div class="flex flex-grow flex-col justify-between">
      <nav>
        <ul class="space-y-2">
          <li>
            <NavLink to="/admin/restaurants">
              <ChefHatIcon />
              Restaurants
            </NavLink>
          </li>
          <li>
            <NavLink to="/admin/users">
              <UsersRoundIcon />
              Users
            </NavLink>
          </li>
        </ul>
      </nav>

      <div class="flex flex-col gap-2">
        <Separator />
        <Button @click="signOut" variant="outline" class="flex w-full items-center gap-2">
          <LogOutIcon class="size-5" />
          Sign out
        </Button>
      </div>
    </div>
  </aside>
</template>
