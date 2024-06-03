<script setup lang="ts">
import NavLink from '@/components/(manage)/common/NavLink.vue';
import Logo from '@/components/Logo.vue';
import {
  CookingPotIcon,
  LayoutDashboardIcon,
  MessageCircleWarningIcon,
  SettingsIcon,
  TicketCheckIcon,
  LogOutIcon,
} from 'lucide-vue-next';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';
import { useRestaurantUser } from '@/composables/useRestaurantUser';

const { signOut } = useRestaurantUser();
</script>

<template>
  <aside class="sticky top-0 flex h-full max-h-svh w-full flex-col px-6 py-5">
    <RouterLink to="/manage">
      <Logo class="mb-8" />
    </RouterLink>

    <div class="flex flex-grow flex-col justify-between">
      <nav>
        <ul class="space-y-2">
          <li>
            <NavLink to="/manage">
              <LayoutDashboardIcon />
              Dashboard
            </NavLink>
          </li>
          <li>
            <NavLink to="/manage/bookings">
              <TicketCheckIcon />
              Bookings
            </NavLink>
          </li>
          <li>
            <NavLink to="/manage/meals">
              <CookingPotIcon />
              Meals
            </NavLink>
          </li>
          <li>
            <NavLink to="/manage/reports">
              <MessageCircleWarningIcon />
              Reports
            </NavLink>
          </li>
        </ul>
      </nav>

      <div class="flex flex-col gap-2">
        <NavLink to="/manage/settings">
          <SettingsIcon />
          Restaurant settings
        </NavLink>
        <Separator />
        <Button @click="signOut" variant="outline" class="flex w-full items-center gap-2">
          <LogOutIcon class="size-5" />
          Sign out
        </Button>
      </div>
    </div>
  </aside>
</template>
