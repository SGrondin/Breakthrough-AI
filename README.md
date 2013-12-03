Breakthrough-AI
===============

School project with teammates [Sacha Tousignant](https://github.com/stousignant) and Alexandre Sanscartier.

AI for a [Breakthrough](http://en.wikipedia.org/wiki/Breakthrough_%28board_game%29) tournament.

This is our AI in all of its glory, with its debug code, comments, relic code... and bugs.
Out of 24 teams in the tournament, we were taken out in the semifinals by the winning team, due to a boolean bug in our [AlphaBeta](http://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning) pruning code, *only when playing black*.
A few minutes before our first match, we decided to comment out the AlphaBeta code when playing black. This decision proved to be crucial to getting to the semifinals and performing so well. Our AI could utilize all 4 cores of the university computers, compared to most of our opponents who didn't know how to multithread complex tree algorithms like this. More than 50% of the time put into this project was by me experimenting with parallel code. On the 5th or 6th iteration, I came up with the current system. It allowed us to see at a depth of 6 or 7 moves into the future, instead of 4 like almost every one of our opponents. In addition to this, Sacha found a brilliant way to detect unbeatable moves up to 8 moves in the future, *in addition to our standard 7 moves*.

It goes without saying that the AlphaBeta bug when playing black cost us the first place. When playing white, our AI outplayed the winners due to our multithreading+heuristics combo. However, we lost against them when playing black; it was excruciating to see 2 pawns in a winning position *that they could not detect because the bug restrained us to 4 moves into the future instead of 6-7*. I'm curious to see what we could have done have we had put more time into it.

Simon Grondin: architecture, multithreading and everything else, except:

[Sacha Tousignant](https://github.com/stousignant): heuristics (Analyzer.java)

Alexandre Sanscartier: bitboard code (Bitsmagic.java)
