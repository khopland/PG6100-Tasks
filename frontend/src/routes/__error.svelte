<script lang="ts">
	import { subscribe } from '../store/user';

	let user = null;
	subscribe((value) => {
		user = value;
	});
</script>

<h2>NOT FOUND: 404</h2>
<p>ERROR: the page you requested in not available.</p>
{#if user}
	{user?.name}
{/if}
