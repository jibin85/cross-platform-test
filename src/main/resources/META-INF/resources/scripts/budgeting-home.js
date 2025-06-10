document.addEventListener('DOMContentLoaded', function () {
    console.log('🚀 DOM Content Loaded - Initializing budgeting home');

    // ✨ Navigation functionality
    document.querySelectorAll('.card').forEach(card => {
        card.addEventListener('click', function() {
            const section = this.getAttribute('data-section');
            console.log('🧭 Navigating to:', section);
            // Add your navigation logic here
        });
    });

    // Logout functionality
    document.getElementById('header-logout-btn').addEventListener('click', function(e) {
        console.log('🚪 Logout clicked');

        if (e) {
            e.preventDefault();
            e.stopPropagation();
        }

        const userEmail = localStorage.getItem('userEmail');

        // Make logout API call
        if (userEmail) {
            console.log('📡 Making logout API call');
            fetch('http://localhost:4141/api/users/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ mail: userEmail }),
            }).catch(error => console.error('❌ Logout API error:', error));
        }

        // Clear any sticky notes data if needed
        if (window.stickyNotesManager) {
            console.log('🧹 Clearing sticky notes data');
            window.stickyNotesManager.notes = [];
            window.stickyNotesManager.originalNoteTexts = {};
        }

        // Clear localStorage
        localStorage.clear();
        console.log('🧹 localStorage cleared');

        // Create logout popup
        const popup = document.createElement('div');
        popup.textContent = 'Logout successful. Redirecting...';
        popup.style.position = 'fixed';
        popup.style.top = '20px';
        popup.style.left = '50%';
        popup.style.transform = 'translateX(-50%)';
        popup.style.backgroundColor = '#a020f0';
        popup.style.color = 'white';
        popup.style.padding = '12px 20px';
        popup.style.borderRadius = '8px';
        popup.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.2)';
        popup.style.fontSize = '16px';
        popup.style.zIndex = '9999';
        document.body.appendChild(popup);
        console.log('📱 Logout popup displayed');

        // Close windows and redirect after 2 seconds
        setTimeout(() => {
            console.log('⏰ Timeout reached, closing windows');
            popup.remove();

            // Close the parent/opener window (dashboard) if it exists
            if (window.opener && !window.opener.closed) {
                console.log('🏠 Closing parent dashboard window');
                window.opener.close();
            }

            // Close the current budgeting window
            console.log('🔚 Closing current budgeting window');
            window.close();
        }, 2000);
    });

    // Sticky Notes functionality
    document.getElementById('sticky-notes-btn').addEventListener('click', function() {
        console.log('📝 Sticky Notes button clicked');

        // Sticky Notes Implementation with External Templates
        class StickyNotesManager {
            constructor() {
                console.log('🏗️ StickyNotesManager constructor called');
                this.isVisible = false;
                this.isDragging = false;
                this.dragOffset = { x: 0, y: 0 };
                this.stickyNotesContainer = null;
                this.notes = this.loadNotes();
                this.templates = {};
                this.originalNoteTexts = {};
                console.log('📊 Initial notes loaded:', this.notes);
                this.init();
            }

            init() {
                console.log('⚙️ Initializing StickyNotesManager');

                // Add event listener to sticky notes button
                const stickyNotesBtn = document.getElementById('sticky-notes-btn');
                if (stickyNotesBtn) {
                    console.log('✅ Found sticky notes button, adding event listener');
                    stickyNotesBtn.addEventListener('click', () => this.toggleStickyNotes());
                } else {
                    console.error('❌ Sticky notes button not found');
                }
            }

            // Template loading utility
            async loadTemplate(templatePath) {
                console.log(`🔄 Loading template: ${templatePath}`);

                if (this.templates[templatePath]) {
                    console.log(`📋 Template found in cache: ${templatePath}`);
                    return this.templates[templatePath];
                }

                try {
                    console.log(`🌐 Fetching template from: ${templatePath}`);
                    const response = await fetch(templatePath);
                    console.log(`📡 Response status: ${response.status} ${response.statusText}`);
                    if (!response.ok) {
                        throw new Error(`Failed to load template: ${templatePath} - Status: ${response.status}`);
                    }
                    const template = await response.text();
                    console.log(`📄 Template loaded successfully: ${templatePath} (${template.length} characters)`);
                    this.templates[templatePath] = template;
                    return template;
                } catch (error) {
                    console.error(`❌ Error loading template ${templatePath}:`, error);
                    return null;
                }
            }

            // Template interpolation utility
            interpolateTemplate(template, data) {
                console.log('🔧 Interpolating template with data:', data);
                const result = template.replace(/\{\{(\w+)\}\}/g, (match, key) => {
                    const value = data[key] !== undefined ? data[key] : match;
                    console.log(`🔀 Replacing {{${key}}} with:`, value);
                    return value;
                });
                console.log('✅ Template interpolation complete');
                return result;
            }

            toggleStickyNotes() {
                console.log(`🔄 Toggling sticky notes - Current visibility: ${this.isVisible}`);
                if (this.isVisible) {
                    this.hideStickyNotes();
                } else {
                    this.showStickyNotes();
                }
            }

            async showStickyNotes() {
                console.log('👁️ Showing sticky notes');

                // First, retrieve notes from server
                await this.retrieveNotesFromServer();
                if (this.stickyNotesContainer) {
                    console.log('📦 Container exists, making visible');
                    this.stickyNotesContainer.style.display = 'block';
                } else {
                    console.log('🏗️ Container doesn\'t exist, creating interface');
                    await this.createStickyNotesInterface();
                }
                this.isVisible = true;
                console.log('✅ Sticky notes now visible');
            }

            async retrieveNotesFromServer() {
                console.log('📥 Retrieving notes from server');

                try {
                    const response = await fetch('api/user/sticky-notes/retrieve', {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });

                    if (!response.ok) {
                        throw new Error(`Retrieve API call failed: ${response.status}`);
                    }

                    const serverNotes = await response.json();
                    console.log('📥 Notes retrieved from server:', serverNotes);

                    // Transform server response to match your local note format
                    this.notes = this.transformServerNotes(serverNotes);
                    console.log('✅ Notes transformed and loaded:', this.notes);

                    return true;
                } catch (error) {
                    console.error('❌ Error retrieving notes from server:', error);
                    // If retrieval fails, keep existing notes or empty array
                    return false;
                }
            }

            transformServerNotes(serverResponse) {
                console.log('🔄 Transforming server response to local format');
                if (!serverResponse || !Array.isArray(serverResponse.stickyNotes)) {
                    console.warn('⚠️ Invalid server response format, returning empty array');
                    return [];
                }

                // Transform and sort by timestamp in descending order (latest first)
                const transformedNotes = serverResponse.stickyNotes
                    .map(serverNote => {
                        const note = {
                            id: serverNote.noteId,
                            text: serverNote.stickyNotesValue || '',
                            timestamp: new Date(serverNote.timestamp).toLocaleString(),
                            color: serverNote.noteColor
                        };

                        // Store original text for save button state management
                        this.originalNoteTexts[note.id] = note.text;

                        return note;
                    })
                    .sort((a, b) => {
                        // Sort by timestamp in descending order (latest first)
                        return new Date(b.timestamp) - new Date(a.timestamp);
                    });
                return transformedNotes;
            }

            hideStickyNotes() {
                console.log('🙈 Hiding sticky notes');
                if (this.stickyNotesContainer) {
                    this.stickyNotesContainer.style.display = 'none';
                    console.log('✅ Sticky notes hidden');
                } else {
                    console.warn('⚠️ Tried to hide non-existent container');
                }
                this.isVisible = false;
            }

            async createStickyNotesInterface() {
                console.log('🏗️ Creating sticky notes interface');
                try {
                    // Load the main container template
                    const containerTemplate = await this.loadTemplate('templates/sticky_notes/sticky-notes-container-template.html');
                    if (!containerTemplate) {
                        console.error('❌ Failed to load sticky notes container template');
                        return;
                    }
                    console.log('📝 Adding sticky notes interface to body');
                    // Add the sticky notes interface to the body
                    document.body.insertAdjacentHTML('beforeend', containerTemplate);
                    this.stickyNotesContainer = document.getElementById('sticky-notes-container');
                    console.log('📦 Container element found:', !!this.stickyNotesContainer);
                    this.setupEventListeners();

                    // Load initial notes content
                    console.log('📄 Loading initial notes content');
                    await this.renderNotesList();
                    console.log('✅ Sticky notes interface created successfully');
                } catch (error) {
                    console.error('❌ Error creating sticky notes interface:', error);
                }
            }

            setupEventListeners() {
                console.log('🎧 Setting up event listeners');
                const header = document.getElementById('sticky-notes-header');
                const closeBtn = document.getElementById('close-sticky-notes');
                const addBtn = document.getElementById('add-note-btn');
                console.log('🔍 Elements found:', {
                    header: !!header,
                    closeBtn: !!closeBtn,
                    addBtn: !!addBtn
                });

                // Dragging functionality
                if (header) {
                    header.addEventListener('mousedown', (e) => this.startDragging(e));
                    console.log('✅ Drag event listener added to header');
                }
                document.addEventListener('mousemove', (e) => this.drag(e));
                document.addEventListener('mouseup', () => this.stopDragging());

                // Close button
                if (closeBtn) {
                    closeBtn.addEventListener('click', () => this.hideStickyNotes());
                    console.log('✅ Close button event listener added');
                }

                // Add note button
                if (addBtn) {
                    addBtn.addEventListener('click', () => this.addNewNote());
                    console.log('✅ Add note button event listener added');
                }

                document.addEventListener('click', (e) => {
                    if (e.target.classList.contains('save-note-btn')) {
                        const noteId = parseInt(e.target.getAttribute('data-note-id'));
                        this.saveIndividualNote(noteId);
                    }
                });

                document.addEventListener('click', (e) => {
                    if (e.target.classList.contains('delete-note-btn')) {
                        const noteId = parseInt(e.target.getAttribute('data-note-id'));
                        this.deleteNoteFromServer(noteId);
                    }
                });
            }

            startDragging(e) {
                console.log('🖱️ Start dragging');
                this.isDragging = true;
                const rect = this.stickyNotesContainer.getBoundingClientRect();
                this.dragOffset = {
                    x: e.clientX - rect.left,
                    y: e.clientY - rect.top
                };
                console.log('📍 Drag offset:', this.dragOffset);
                document.body.style.userSelect = 'none';
            }

            drag(e) {
                if (!this.isDragging) return;

                const x = e.clientX - this.dragOffset.x;
                const y = e.clientY - this.dragOffset.y;

                // Keep within viewport bounds
                const maxX = window.innerWidth - this.stickyNotesContainer.offsetWidth;
                const maxY = window.innerHeight - this.stickyNotesContainer.offsetHeight;
                const constrainedX = Math.max(0, Math.min(x, maxX));
                const constrainedY = Math.max(0, Math.min(y, maxY));
                this.stickyNotesContainer.style.left = constrainedX + 'px';
                this.stickyNotesContainer.style.top = constrainedY + 'px';
                this.stickyNotesContainer.style.right = 'auto';
                this.stickyNotesContainer.style.bottom = 'auto';
            }

            stopDragging() {
                if (this.isDragging) {
                    console.log('🛑 Stop dragging');
                    this.isDragging = false;
                    document.body.style.userSelect = '';
                }
            }

            // Updated addNewNote method to include API call
            async addNewNote() {
                console.log('➕ Adding new note');
                const newNote = {
                    id: Date.now(),
                    text: '',
                    timestamp: new Date().toLocaleString(),
                    color: this.getRandomNoteColor()
                };

                console.log('📝 New note created:', newNote);
                this.notes.unshift(newNote);

                // Initialize original text as empty for new note
                this.originalNoteTexts[newNote.id] = '';
                this.saveNotes();
                await this.renderNotesList();

                // Enable save button for new note and focus on textarea
                setTimeout(() => {
                    this.enableSaveButton(newNote.id);
                    const newNoteTextarea = document.querySelector(`#note-${newNote.id} textarea`);
                    if (newNoteTextarea) {
                        console.log('🎯 Focusing on new note textarea');
                        newNoteTextarea.focus();
                    } else {
                        console.warn('⚠️ Could not find textarea for new note');
                    }
                }, 100);
            }

            async saveIndividualNote(noteId) {
                console.log('💾 Saving individual note:', noteId);
                const note = this.notes.find(n => n.id === noteId);
                if (!note || !note.text || note.text.trim() === '') {
                    console.warn('⚠️ Note not found or empty, skipping save');
                    return false;
                }

                try {
                    const payload = {
                        noteId: note.id,
                        noteColor: note.color,
                        timestamp: Date.now(),
                        stickyNoteValue: note.text
                    };
                    const response = await fetch('api/user/sticky-notes/insert', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(payload)
                    });
                    if (!response.ok) {
                        throw new Error(`API call failed: ${response.status}`);
                    }
                    console.log('✅ Note saved successfully in server');
                    // Store the current text as the new "original" text and disable save button
                    this.originalNoteTexts[noteId] = note.text;
                    this.disableSaveButton(noteId);

                    return true;
                } catch (error) {
                    console.error('❌ Error saving note:', error);
                    return false;
                }
            }

            async deleteNoteFromServer(noteId) {
                console.log('🗑️ Deleting note from server:', noteId);

                try {
                    const payload = {
                        noteId: noteId
                    };
                    const response = await fetch('api/user/sticky-notes/delete', {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(payload)
                    });
                    if (!response.ok) {
                        throw new Error(`Delete API call failed: ${response.status}`);
                    }
                    console.log('✅ Note deleted from server successfully');
                    // Remove from local array and re-render
                    await this.deleteNote(noteId);
                    return true;
                } catch (error) {
                    console.error('❌ Error deleting note from server:', error);
                    return false;
                }
            }

            async deleteNote(noteId) {
                console.log('🗑️ Deleting note:', noteId);
                const initialCount = this.notes.length;
                this.notes = this.notes.filter(note => note.id !== noteId);
                console.log(`📊 Notes count: ${initialCount} → ${this.notes.length}`);
                this.saveNotes();
                await this.renderNotesList();
            }

            updateNote(noteId, text) {
                console.log('✏️ Updating note:', noteId, 'with text length:', text.length);
                const note = this.notes.find(n => n.id === noteId);
                if (note) {
                    // Clean the text (remove extra spaces and normalize whitespace)
                    const cleanedText = text.trim().replace(/\s+/g, ' ');
                    const originalText = this.originalNoteTexts[noteId] || '';
                    const cleanedOriginalText = originalText.trim().replace(/\s+/g, ' ');

                    // Only update if the cleaned text has actually changed
                    if (note.text !== text) {
                        note.text = text;
                        note.timestamp = new Date().toLocaleString();
                        console.log('✅ Note updated:', { id: noteId, timestamp: note.timestamp });
                        this.saveNotes();

                        // Enable save button if content meaningfully changed
                        if (cleanedText !== cleanedOriginalText) {
                            this.enableSaveButton(noteId);
                        }
                    } else {
                        console.log('ℹ️ Note text unchanged, skipping update:', noteId);
                    }
                } else {
                    console.warn('⚠️ Note not found for update:', noteId);
                }
            }

            // Enable save button
            enableSaveButton(noteId) {
                const saveBtn = document.querySelector(`[data-note-id="${noteId}"].save-note-btn`);
                if (saveBtn) {
                    // Reset styles to active state
                    saveBtn.style.pointerEvents = 'auto';
                    saveBtn.style.opacity = '1';
                    saveBtn.style.cursor = 'pointer';
                    saveBtn.style.background = 'rgba(0, 0, 0, 0.1)';
                    saveBtn.style.color = 'rgba(0, 0, 0, 0.9)';
                    console.log('✅ Save button enabled for note:', noteId);
                }
            }

            // Disable save button
            disableSaveButton(noteId) {
                const saveBtn = document.querySelector(`[data-note-id="${noteId}"].save-note-btn`);
                if (saveBtn) {
                    // Apply inline styles to simulate "disabled" look
                    saveBtn.style.pointerEvents = 'none';
                    saveBtn.style.opacity = '0.5';
                    saveBtn.style.cursor = 'default';
                    saveBtn.style.background = 'rgba(0, 0, 0, 0.1)';
                    saveBtn.style.color = 'rgba(0, 0, 0, 0.7)';
                    console.log('🔒 Save button visually disabled for note:', noteId);
                }
            }

            async renderNotesList() {
                console.log('🎨 Rendering notes list');
                const notesList = document.getElementById('notes-list');
                if (!notesList) {
                    console.error('❌ Notes list element not found');
                    return;
                }
                console.log(`📝 Notes to render: ${this.notes.length}`);
                if (this.notes.length === 0) {
                    console.log('📭 No notes, loading empty state template');
                    // Load empty state template
                    const emptyStateTemplate = await this.loadTemplate('templates/sticky_notes/empty-state-template.html');
                    if (emptyStateTemplate) {
                        notesList.innerHTML = emptyStateTemplate;
                        console.log('✅ Empty state template loaded');
                    } else {
                        console.error('❌ Failed to load empty state template');
                    }
                } else {
                    console.log('📝 Loading notes templates');
                    // Load and render notes
                    const notesHtml = await this.renderNotes();
                    notesList.innerHTML = notesHtml;
                    console.log('✅ Notes rendered successfully');
                }
            }

            async renderNotes() {
                console.log('🎨 Rendering individual notes');
                try {
                    // Load the note item template
                    const noteTemplate = await this.loadTemplate('templates/sticky_notes/note-item-template.html');
                    if (!noteTemplate) {
                        console.error('❌ Failed to load note template');
                        return '';
                    }
                    console.log(`🔄 Processing ${this.notes.length} notes`);
                    // Generate HTML for each note
                    const notesHtml = this.notes.map((note, index) => {
                        console.log(`🎨 Rendering note ${index + 1}/${this.notes.length}:`, note.id);
                        return this.interpolateTemplate(noteTemplate, {
                            noteId: note.id,
                            noteColor: note.color,
                            timestamp: note.timestamp,
                            noteText: note.text,
                            saveButtonId: `save-${note.id}`
                        });
                    }).join('');
                    console.log(`✅ Generated HTML for ${this.notes.length} notes (${notesHtml.length} characters)`);
                    return notesHtml;
                } catch (error) {
                    console.error('❌ Error rendering notes:', error);
                    return '';
                }
            }

            getRandomNoteColor() {
                const colors = [
                    '#FFE082', // Yellow
                    '#FFAB91', // Orange
                    '#A5D6A7', // Green
                    '#90CAF9', // Blue
                    '#F8BBD9', // Pink
                    '#D1C4E9', // Purple
                    '#FFCDD2', // Red
                    '#C8E6C9'  // Light Green
                ];
                const selectedColor = colors[Math.floor(Math.random() * colors.length)];
                console.log('🎨 Selected random color:', selectedColor);
                return selectedColor;
            }

            loadNotes() {
                console.log('📂 Loading notes from memory');
                // Since we can't use localStorage, return empty array
                // In a real implementation, you'd load from localStorage:
                // return JSON.parse(localStorage.getItem('stickyNotes') || '[]');
                const notes = [];
                console.log('📊 Loaded notes:', notes);
                return notes;
            }

            saveNotes() {
                // Since we can't use localStorage, we'll just keep notes in memory
                // In a real implementation, you'd save to localStorage:
                // localStorage.setItem('stickyNotes', JSON.stringify(this.notes));
                console.log('💾 Notes saved to memory:', this.notes);
            }
        }

        console.log('🏗️ Creating new StickyNotesManager instance');
        // Initialize the sticky notes manager immediately since DOM is already loaded
        if (!window.stickyNotesManager) {
            window.stickyNotesManager = new StickyNotesManager();
            console.log('✅ StickyNotesManager instance created and stored globally');
        } else {
            console.log('ℹ️ StickyNotesManager already exists, reusing instance');
        }
    });
});