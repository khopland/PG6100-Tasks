<script lang="ts">
	import { signout, subscribe } from '../store/user';

	let user;
	subscribe((value) => (user = value));
</script>

<ul id="menu">
	{#if user}
		<li><a href="collection">My Collection</a></li>
		|
		<li><a href="/play">Play</a></li>
		|
		<li><a href="/leaderboard">Leaderboard</a></li>
		|
		<li><a href="/" on:click|preventDefault={signout}>Logout</a></li>
	{:else}
		<li><p>You are not logged in</p></li>
		<li><a href="/signup">Sign up</a></li>
		|
		<li><a href="/login">Log in</a></li>
	{/if}
	|
	<li><a href="/">home</a></li>
</ul>

<div>
	<h1>Monsters</h1>
	<span>The Card Game</span>
	<slot />
</div>

<style>
	ul#menu li {
		display: inline;
	}
</style>
