<script lang="ts">
	import { onMount } from 'svelte';
	import Fetch from '$lib/fetch';
	import { score } from '../types/score';

	let next: string = null;
	let error: string = null;
	let scores: score[] = [];

	onMount(async () => {
		await fetchScores();
	});
	const fetchScores = async () => {
		try {
			const response = await Fetch(next ? next : '/api/scores');
			const payload = await response.json();
			if (response.status === 200) {
				error = null;
				scores = [...scores, ...payload.data.list];
				next = payload.data.next;
			} else {
				error = `Issue with HTTP connection: status code ${response.status}. ${payload.error}`;
				scores = [];
				next = null;
			}
		} catch (err) {
			error = 'ERROR when retrieving scores: ' + err;
			scores = [];
			next = null;
			return;
		}
	};
</script>

<div>
	<h2>LeaderBoard</h2>
	{#if error}
		<p>{error}</p>
	{:else if !scores || scores.length === 0}
		<p>There is no leaderboard info</p>
	{:else}
		<div>
			<table>
				<thead>
					<tr>
						<th>Player</th>
						<th>Victories/Draws/Defeats</th>
						<th>Score</th>
					</tr>
				</thead>
				<tbody>
					{#each scores as s}
						<tr>
							<td>{s.userId}</td>
							<td>{`${s.victories}/${s.draws}${s.defeats}`}</td>
							<td>{s.score}</td>
						</tr>
					{/each}
				</tbody>
			</table>
		</div>
	{/if}
	{#if next}
		<button onClick={fetchScores}>Next</button>
	{/if}
</div>
