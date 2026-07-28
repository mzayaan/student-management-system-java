# Student Management System

A Java Swing desktop application for managing student records, built on a
hand-written singly linked list rather than the collections framework — the
point of the exercise was implementing the data structure and its operations
directly. Written for the BSc (Hons) Software Engineering programme at the
University of Technology, Mauritius.

**▶ [Try it in your browser](https://mzayaan.github.io/student-management-system-java/)**
— no install needed. The Java bytecode runs client-side through
[CheerpJ](https://cheerpj.com/), so the first load pulls down a JVM and takes a
few seconds.

## Features

- **Add, update and delete** student records by student number.
- **Search** by student number or name, with results shown in the display area.
- **Sort** ascending or descending by student number, and A–Z or Z–A by name.
- **Display all** records at any time.
- **File persistence** — records are written to `students.txt` and reloaded on
  startup.

## How it's put together

| Class | Responsibility |
| --- | --- |
| `StudentManagementSystem1` | Swing UI, event handling, input validation |
| `StudentLinkedList` | Singly linked list — insert, update, delete, search, sort, file I/O |
| `StudentLinkedList.Node` | A single record and its pointer to the next |

Sorting is implemented over the linked list itself rather than by copying into
an array, so it works on the node chain directly.

### The two source files

`StudentManagementSystem1.java` is the current version and the one that gets
built and deployed. `StudentManagementSystem.java` is an earlier, smaller
iteration kept for reference — it has no search or sort. The two can't be
compiled together, since both declare a top-level `StudentLinkedList`.

## Run it on your machine

Needs a JDK (17 or newer). No build tool, no dependencies.

```bash
git clone https://github.com/mzayaan/student-management-system-java
cd student-management-system-java
javac StudentManagementSystem1.java
java StudentManagementSystem1
```

Run it from the project directory so `students.txt` resolves.

## Deployment

`.github/workflows/deploy.yml` compiles the source, packages a runnable
`student-management.jar`, and publishes it to GitHub Pages alongside
`web/index.html`, which boots the JAR with CheerpJ.

The browser build rewrites one path at compile time, because relative paths
don't resolve inside CheerpJ's virtual filesystem:

| Source | Browser build | Why |
| --- | --- | --- |
| `students.txt` | `/files/students.txt` | `/files/` is writable, backed by IndexedDB |

So records added in the browser persist between visits, in that browser only.
The committed source is untouched, so the desktop version still runs unchanged.
