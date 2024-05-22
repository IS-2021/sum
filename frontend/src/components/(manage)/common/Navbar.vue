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
  <aside class="w-full h-svh px-6 py-5">
    <RouterLink to="/manage">
      <Logo class="mb-8" />
    </RouterLink>

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
        <li>
          <NavLink to="/manage/settings">
            <SettingsIcon />
            Restaurant settings
          </NavLink>
        </li>
      </ul>
    </nav>
  </aside>
</template>
