<script lang="ts">
import { defineComponent } from 'vue';
import NavLink from '@/components/(manage)/common/NavLink.vue';
import Logo from '@/components/Logo.vue';
import {
  CookingPotIcon,
  LayoutDashboardIcon,
  MessageCircleWarningIcon,
  SettingsIcon,
  TicketCheckIcon,
} from 'lucide-vue-next';

export default defineComponent({
  name: 'Navbar',
  components: {
    LayoutDashboardIcon,
    TicketCheckIcon,
    CookingPotIcon,
    MessageCircleWarningIcon,
    SettingsIcon,
    Logo,
    NavLink,
  },
});
</script>

<template>
  <aside class="w-full max-h-svh h-full px-6 py-5 sticky top-0 flex flex-col">
    <RouterLink to="/manage">
      <Logo class="mb-8" />
    </RouterLink>

    <div class="flex flex-col justify-between flex-grow">
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

      <NavLink to="/manage/settings">
        <SettingsIcon />
        Restaurant settings
      </NavLink>
    </div>
  </aside>
</template>
