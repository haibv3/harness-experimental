# Harness CLI Usage Guide

The Harness CLI (`scripts/bin/harness-cli`) is the primary operational tool for
interacting with the durable layer (SQLite database `harness.db`). This guide
documents all commands with real examples from this repository.

## Quick Reference

```bash
# Initialize database (first time only)
scripts/bin/harness-cli init

# Record an intake
scripts/bin/harness-cli intake --type <TYPE> --summary "..." --lane <LANE>

# Add a story
scripts/bin/harness-cli story add --id US-XXX --title "..." --lane <LANE>

# Update story status and proof
scripts/bin/harness-cli story update --id US-XXX --status implemented --unit 1 --platform 1

# Add a decision
scripts/bin/harness-cli decision add --id NNNN-slug --title "..." --status accepted

# Record a trace
scripts/bin/harness-cli trace --summary "..." --intake N --story US-XXX --outcome completed

# Query the matrix
scripts/bin/harness-cli query matrix

# Query decisions
scripts/bin/harness-cli query decisions
```

---

## Commands

### `init` — Create Database

Creates `harness.db` if it doesn't exist.

```bash
scripts/bin/harness-cli init
```

**When to use:** First time setting up the harness in a new repo.

---

### `intake` — Record Feature Intake

Classify incoming work using the intake process from `docs/FEATURE_INTAKE.md`.

```bash
scripts/bin/harness-cli intake \
  --type <INPUT_TYPE> \
  --summary "Brief description" \
  --lane <tiny|normal|high-risk> \
  [--flags "flag1,flag2"] \
  [--docs "path/to/doc1.md,path/to/doc2.md"] \
  [--story US-XXX] \
  [--notes "Additional context"]
```

#### Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--type` | ✅ | Intake type (see [Intake Types](#intake-types)) |
| `--summary` | ✅ | Brief description of the work |
| `--lane` | ✅ | Risk lane: `tiny`, `normal`, or `high-risk` |
| `--flags` | ❌ | Comma-separated risk flags (see `docs/FEATURE_INTAKE.md`) |
| `--docs` | ❌ | Comma-separated paths to relevant docs |
| `--story` | ❌ | Associated story ID (if known) |
| `--notes` | ❌ | Additional context or constraints |

#### Intake Types

From actual usage in this repo (`harness.db`):

- `harness_improvement` — Improvements to the harness itself
- `new_initiative` — New feature or capability
- `spec_slice` — Executing a story or slice of work

**Example (from this repo):**

```bash
# Intake #5: Scaffold all 3 codebases
scripts/bin/harness-cli intake \
  --type "new initiative" \
  --summary "Scaffold all 3 codebases with build config: Android (multi-module, Hilt, KSP, gradle kts, libs.versions.toml), iOS (Xcode, SwiftUI), desktop-kmp (KMP shared, Compose Desktop)" \
  --lane normal \
  --flags "cross-platform,release/distribution" \
  --docs "docs/ARCHITECTURE.md,docs/decisions/0010-mobile-stack.md,docs/decisions/0011-desktop-stack.md" \
  --notes "No SDK/toolchain present; scaffold files only, no actual build"
```

---

### `story add` — Add a Story

Register a new story in the test matrix.

```bash
scripts/bin/harness-cli story add \
  --id US-XXX \
  --title "Story title" \
  --lane <tiny|normal|high-risk> \
  [--contract "path/to/spec.md"] \
  [--verify "verification command"] \
  [--notes "Additional context"]
```

#### Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--id` | ✅ | Story ID (e.g., `US-020`) |
| `--title` | ✅ | Story title |
| `--lane` | ✅ | Risk lane: `tiny`, `normal`, or `high-risk` |
| `--contract` | ❌ | Comma-separated paths to product contract docs |
| `--verify` | ❌ | Command to verify the story (e.g., `./scripts/test.sh`) |
| `--notes` | ❌ | Additional context |

**Example (from this repo):**

```bash
# US-020: Scaffold Three Codebases
scripts/bin/harness-cli story add \
  --id US-020 \
  --title "Scaffold Three Codebases" \
  --lane normal \
  --contract "docs/decisions/0010-mobile-stack.md,docs/decisions/0011-desktop-stack.md" \
  --notes "Scaffold Android (multi-module, Hilt, KSP, MVI), iOS (Swift/SwiftUI), desktop-kmp (KMP+Compose); no SDK present, files only"
```

---

### `story update` — Update Story Status and Proof

Update story status and test proof flags.

```bash
scripts/bin/harness-cli story update \
  --id US-XXX \
  [--status <STATUS>] \
  [--evidence "Evidence description"] \
  [--unit <0|1>] \
  [--integration <0|1>] \
  [--e2e <0|1>] \
  [--platform <0|1>] \
  [--verify "verification command"]
```

#### Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--id` | ✅ | Story ID |
| `--status` | ❌ | Story status (see [Story Status](#story-status)) |
| `--evidence` | ❌ | Evidence of completion |
| `--unit` | ❌ | Unit test proof: `0` (no) or `1` (yes) |
| `--integration` | ❌ | Integration test proof: `0` or `1` |
| `--e2e` | ❌ | E2E test proof: `0` or `1` |
| `--platform` | ❌ | Platform-specific proof: `0` or `1` |
| `--verify` | ❌ | Verification command |

**IMPORTANT:** Proof flags use **numeric booleans** (`0` or `1`), not `yes`/`no`.

#### Story Status

From actual usage in this repo:

- `implemented` — Story is complete

**Example (from this repo):**

```bash
# Mark US-020 as implemented with platform proof
scripts/bin/harness-cli story update \
  --id US-020 \
  --status implemented \
  --unit 0 \
  --integration 0 \
  --e2e 0 \
  --platform 1 \
  --evidence "3 codebases scaffolded: Android (15 files, multi-module, Hilt, KSP, MVI), iOS (Swift/SwiftUI, Package.swift), desktop-kmp (KMP+Compose, 6 files)"
```

---

### `decision add` — Add a Decision

Register an architectural decision record.

```bash
scripts/bin/harness-cli decision add \
  --id NNNN-slug \
  --title "Decision title" \
  [--status <STATUS>] \
  [--doc "path/to/decision.md"] \
  [--verify "verification command"] \
  [--predicted "predicted outcome"] \
  [--notes "Additional context"]
```

#### Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--id` | ✅ | Decision ID (e.g., `0010-mobile-stack`) |
| `--title` | ✅ | Decision title |
| `--status` | ❌ | Decision status (default: `accepted`) — see [Decision Status](#decision-status) |
| `--doc` | ❌ | Path to decision markdown file |
| `--verify` | ❌ | Command to verify the decision is followed |
| `--predicted` | ❌ | Predicted outcome or impact |
| `--notes` | ❌ | Additional context |

#### Decision Status

From actual usage in this repo:

- `accepted` — Decision is approved and active
- `superseded` — Decision has been replaced by a newer one

**Example (from this repo):**

```bash
# Decision 0010: Mobile Stack (native Kotlin/Swift)
scripts/bin/harness-cli decision add \
  --id 0010-mobile-stack \
  --title "Mobile Stack" \
  --status accepted \
  --doc docs/decisions/0010-mobile-stack.md \
  --notes "Native per platform: Android (Kotlin/Compose/MVI/multi-module/Hilt/KSP) + iOS (Swift/SwiftUI)"
```

---

### `backlog add` — Add Backlog Item

Record a harness improvement or technical debt item.

```bash
scripts/bin/harness-cli backlog add \
  --title "Backlog item title" \
  [--while "Discovered while doing X"] \
  [--pain "Pain point description"] \
  [--suggestion "Suggested fix"] \
  [--risk <tiny|normal|high-risk>] \
  [--predicted "predicted outcome"] \
  [--notes "Additional context"]
```

#### Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--title` | ✅ | Backlog item title |
| `--while` | ❌ | Context: what were you doing when you discovered this? |
| `--pain` | ❌ | Pain point description |
| `--suggestion` | ❌ | Suggested fix or improvement |
| `--risk` | ❌ | Risk lane: `tiny`, `normal`, or `high-risk` |
| `--predicted` | ❌ | Predicted outcome if fixed |
| `--notes` | ❌ | Additional context |

**Example (from this repo):**

```bash
# Backlog #1: Surface enum values in CLI --help
scripts/bin/harness-cli backlog add \
  --title "Surface story/trace enum values in CLI --help" \
  --while "Recording trace #1, discovered status enum constraint via SQLite error" \
  --pain "CLI --help doesn't show valid enum values for --status, --outcome; must guess or read schema" \
  --suggestion "Add enum values to --help text for story/trace status/outcome fields" \
  --risk tiny
```

---

### `trace` — Record Agent Execution Trace

Record a trace of agent work for quality scoring and friction analysis.

```bash
scripts/bin/harness-cli trace \
  --summary "Brief summary of work done" \
  [--intake N] \
  [--story US-XXX] \
  [--agent <agent-name>] \
  [--outcome <OUTCOME>] \
  [--duration <seconds>] \
  [--tokens <count>] \
  [--friction "Friction description"] \
  [--actions "action1,action2"] \
  [--read "file1.md,file2.md"] \
  [--changed "file1.kt,file2.swift"] \
  [--decisions "Decision summary"] \
  [--errors "Error description"] \
  [--notes "Additional context"]
```

#### Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--summary` | ✅ | Brief summary of work done |
| `--intake` | ❌ | Associated intake ID |
| `--story` | ❌ | Associated story ID |
| `--agent` | ❌ | Agent name (e.g., `devin`, `claude`, `cursor`) |
| `--outcome` | ❌ | Trace outcome (see [Trace Outcome](#trace-outcome)) |
| `--duration` | ❌ | Duration in seconds |
| `--tokens` | ❌ | Token count estimate |
| `--friction` | ❌ | Harness friction encountered |
| `--actions` | ❌ | Comma-separated actions taken (e.g., `read,write,edit,exec`) |
| `--read` | ❌ | Comma-separated files read |
| `--changed` | ❌ | Comma-separated files changed |
| `--decisions` | ❌ | Key decisions made during execution |
| `--errors` | ❌ | Errors encountered |
| `--notes` | ❌ | Additional context |

#### Trace Outcome

From actual usage in this repo:

- `completed` — Trace completed successfully

**Example (from this repo):**

```bash
# Trace #5: Scaffold all 3 codebases
scripts/bin/harness-cli trace \
  --summary "Scaffold all 3 codebases: Android (multi-module, Hilt, KSP, MVI base, gradle kts, libs.versions.toml), iOS (Swift/SwiftUI, Package.swift), desktop-kmp (KMP shared + Compose Desktop); 24 files created total; no SDK present, files only" \
  --intake 5 \
  --story US-020 \
  --agent devin \
  --outcome completed \
  --actions "write,exec" \
  --read "docs/ARCHITECTURE.md,docs/decisions/0010-mobile-stack.md,docs/decisions/0011-desktop-stack.md,docs/stories/US-020-scaffold-three-codebases.md" \
  --changed "android/build.gradle.kts,android/settings.gradle.kts,android/gradle/libs.versions.toml,android/app/build.gradle.kts,android/core/core-ui/build.gradle.kts,android/core/core-ui/src/main/kotlin/com/example/core/ui/mvi/MviViewModel.kt,ios/Package.swift,ios/App/HarnessApp.swift,desktop-kmp/build.gradle.kts,desktop-kmp/shared/build.gradle.kts,desktop-kmp/desktop/src/main/kotlin/com/example/desktop/Main.kt,docs/stories/US-020-scaffold-three-codebases.md" \
  --decisions "Scaffolded 3 codebases per scenario A; Android uses Hilt+KSP (not KAPT), MVI base in core-ui; iOS uses Swift 6+SwiftUI; desktop KMP shares Greeting placeholder" \
  --friction "None; scaffold-only task, no build attempted"
```

---

### `query` — Query Harness Data

Query the durable layer for various views.

```bash
# Test matrix (stories with proof status)
scripts/bin/harness-cli query matrix

# Backlog items
scripts/bin/harness-cli query backlog

# Decision records
scripts/bin/harness-cli query decisions

# Recent intakes
scripts/bin/harness-cli query intakes

# Recent traces
scripts/bin/harness-cli query traces

# Traces with friction
scripts/bin/harness-cli query friction

# Summary counts
scripts/bin/harness-cli query stats

# Arbitrary SQL
scripts/bin/harness-cli query sql "SELECT * FROM story WHERE status = 'implemented'"
```

**Example output (from this repo):**

```bash
$ scripts/bin/harness-cli query stats
=== Harness Stats ===
intakes  stories  decisions  backlog_items  traces
-------  -------  ---------  -------------  ------
5        2        5          1              5

$ scripts/bin/harness-cli query matrix
id      title                            status       unit  integ  e2e  plat  evidence
------  -------------------------------  -----------  ----  -----  ---  ----  --------
US-019  Select Cross-Platform App Stack  implemented  no    no     no   no    Scenario A locked: ...
US-020  Scaffold Three Codebases         implemented  no    no     no   yes   3 codebases scaffolded: ...

$ scripts/bin/harness-cli query decisions
id                                 title                          status      last_verified_at  last_verified_result
---------------------------------  -----------------------------  ----------  ----------------  --------------------
0007-mobile-desktop-app-focus      Mobile And Desktop App Focus   accepted
0008-cross-platform-stack-flutter  Cross-Platform Stack: Flutter  superseded
0009-hybrid-mobile-desktop-split   Hybrid Mobile-Desktop Split    accepted
0010-mobile-stack                  Mobile Stack                   accepted
0011-desktop-stack                 Desktop Stack                  accepted
```

---

## Common Workflows

### 1. Record New Work (Intake → Story → Trace)

```bash
# 1. Record intake
scripts/bin/harness-cli intake \
  --type "new initiative" \
  --summary "Add user authentication" \
  --lane normal \
  --flags "security,existing behavior"

# 2. Add story
scripts/bin/harness-cli story add \
  --id US-021 \
  --title "Add User Authentication" \
  --lane normal

# 3. Do the work...

# 4. Update story status
scripts/bin/harness-cli story update \
  --id US-021 \
  --status implemented \
  --unit 1 \
  --integration 1 \
  --platform 1 \
  --evidence "Auth flow implemented with unit + integration tests"

# 5. Record trace
scripts/bin/harness-cli trace \
  --summary "Implemented user authentication with JWT" \
  --intake 6 \
  --story US-021 \
  --agent devin \
  --outcome completed \
  --actions "read,write,edit,exec" \
  --changed "auth/jwt.kt,auth/AuthService.kt,tests/AuthTest.kt"
```

### 2. Record a Decision

```bash
# 1. Write decision markdown
# docs/decisions/0012-jwt-auth.md

# 2. Register decision
scripts/bin/harness-cli decision add \
  --id 0012-jwt-auth \
  --title "Use JWT for Authentication" \
  --status accepted \
  --doc docs/decisions/0012-jwt-auth.md
```

### 3. Check Current State

```bash
# Quick overview
scripts/bin/harness-cli query stats

# See all stories and their proof status
scripts/bin/harness-cli query matrix

# See all decisions
scripts/bin/harness-cli query decisions

# See recent traces
scripts/bin/harness-cli query traces
```

---

## Known Gaps (Backlog #1)

The CLI `--help` output does **not** currently show valid enum values for:

- `story update --status` (valid: `implemented`)
- `trace --outcome` (valid: `completed`)
- `decision add --status` (valid: `proposed`, `accepted`, `superseded`, `rejected`)

These values are enforced by SQLite CHECK constraints but not surfaced in
`--help`. See `scripts/bin/harness-cli query sql "PRAGMA table_info(story)"` to
inspect schema constraints.

**Workaround:** Refer to this document or the schema directly.

---

## Platform-Specific Notes

### macOS / Linux

```bash
scripts/bin/harness-cli <command>
```

### Windows

```powershell
.\scripts\bin\harness-cli.exe <command>
```

---

## See Also

- `docs/HARNESS.md` — Harness philosophy and operating model
- `docs/FEATURE_INTAKE.md` — Intake classification process
- `docs/TEST_MATRIX.md` — Proof tier requirements
- `docs/TRACE_SPEC.md` — Trace quality scoring
