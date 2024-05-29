<script setup lang="ts">
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { LogOut, MapPinIcon, StarIcon, TicketCheckIcon, UserIcon } from 'lucide-vue-next';
import { useUser } from '@/composables/useUser';
import { formatAddress } from '@/lib/googleMaps';

const { user, signOut } = useUser();
</script>

<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child class="bg-opacity-0 hover:bg-opacity-15 hover:text-white">
      <Button variant="ghost"> <UserIcon class="h-4 w-4 mr-3" /> john.cena </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent class="w-56">
      <DropdownMenuLabel>Your Location</DropdownMenuLabel>

      <DropdownMenuItem disabled v-if="user?.address">
        {{ formatAddress(user.address) }}
      </DropdownMenuItem>
      <DropdownMenuItem as-child>
        <RouterLink to="/settings">
          <MapPinIcon class="mr-2 h-4 w-4" />
          <span>Change location</span>
        </RouterLink>
      </DropdownMenuItem>

      <DropdownMenuSeparator />

      <DropdownMenuItem as-child>
        <RouterLink to="/favourites">
          <StarIcon class="mr-2 h-4 w-4" />
          <span>Favourites</span>
        </RouterLink>
      </DropdownMenuItem>
      <DropdownMenuItem as-child>
        <RouterLink to="/bookings">
          <TicketCheckIcon class="mr-2 h-4 w-4" />
          <span>Bookings</span>
        </RouterLink>
      </DropdownMenuItem>

      <DropdownMenuSeparator />

      <DropdownMenuItem @select="signOut">
        <LogOut class="mr-2 h-4 w-4" />
        <span>Sign out</span>
      </DropdownMenuItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>
