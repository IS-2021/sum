<template>
  <footer
    class="w-full bottom-0 border border-transparent bg-neutral-800 flex items-center justify-left p-10 text-neutral-50"
  >
    <div class="flex flex-col items-center text-xs gap-3">
      <div class="w-16 h-16">
        <img src="@/assets/logo.svg" alt="Logo" />
      </div>
      <p>&copy; 2024</p>
    </div>
    <div class="flex-grow"></div>
    <div class="flex flex-col w-32">
      <nav>
        <ul class="flex flex-col items-center gap-3">
          <li>
            <RouterLink to="/">Home</RouterLink>
          </li>
          <li>
            <Button as-child>
              <RouterLink to="/sign-in">Sign in</RouterLink>
            </Button>
          </li>
        </ul>
      </nav>
    </div>
  </footer>
</template>
